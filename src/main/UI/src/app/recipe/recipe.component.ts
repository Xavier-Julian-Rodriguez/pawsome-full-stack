import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {RecipeService} from "../recipe.service";
import {HttpHeaders} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {SharedService} from "../shared.service";
import {Pet} from "../models/pet.model";
import {UtilsService} from "../utils.service";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";
import {marked} from "marked";
import {PetService} from "../pet.service";
import {Recipe} from "../recipe.model";
import {response} from "express";
import {ParsedRecipe} from "../models/parsed-recipe.model";

@Component({
  selector: 'app-recipe',
  standalone: true,
  imports: [NgIf, FormsModule, NgForOf],
  templateUrl: './recipe.component.html',
  styleUrl: './recipe.component.css'
})
export class RecipeComponent implements OnInit {

  pet: Pet | null = null;
  recipes: ParsedRecipe[] = [];
  title: string = "";
  ingredients: string = "";
  ingredientsResponse: string = "";
  instructions: string = "";
  petId: number | null=null;
  errorMessage: string = "";
  successMessage: string = "";
  content: SafeHtml = '';
  keyword:string = '';
  recipesList: ParsedRecipe[] = [];

  constructor(private recipeService: RecipeService, private utilService: UtilsService,private sharedService: SharedService,private route: ActivatedRoute, private sanitizer: DomSanitizer, private petService: PetService, private router: Router) {
  }

  ngOnInit(): void {
    this.extractPetIdFromRoute();
    if (this.petId !== null) {
      this.loadPet(this.petId);
    }
    this.loadRecipes();
  }

  extractPetIdFromRoute(): void {
    const petIdString = this.route.snapshot.paramMap.get('petId');
    console.log('Extracted petIdString:', petIdString);
    if (petIdString) {
      const petId = parseInt(petIdString, 10);
      if (!isNaN(petId)) {
        this.petId = petId;
        console.log('Valid petId:', this.petId);
      } else {
        console.error('Invalid pet ID:', petIdString);
      }
    } else {
      console.error('Pet ID not found in route');
    }
  }

  deletePet(): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
      this.petService.deletePet(this.petId).subscribe((response) => {
        console.log("Pet deleted successfully");
        this.router.navigate(["/pets"]);
      }, error => {
        console.log("Error deleting pet: ", error);
      })
  };

  loadPet(id: number): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    const validPetId = this.petId !== null ? this.petId : 0;
    this.recipeService.getPet(validPetId).subscribe({
      next: pet => {
        this.pet = {
          ...pet,
          image: pet.image ? `data:image/jpeg;base64,${this.utilService.arrayBufferToBase64(pet.image)}` : null
        };
      },
      error: err => {
        console.error('Error loading pet', err);
      }
    })
  }

  async createRecipe(): Promise<void> {
    const token = localStorage.getItem('jwtToken');
    console.log('JWT Token:', token);
    const validPetId = this.petId !== null ? this.petId : 0;

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    console.log('Headers:', headers);

      this.recipeService.createRecipe(this.title, this.ingredientsResponse, this.instructions, validPetId).subscribe( {
        next: (response) => {
          console.log('Recipe created successfully', response);
          this.successMessage = 'Successfully saved recipe.'
          this.loadRecipes();
        },
        error: (error) => {
          console.error('Error creating recipe', error);
        },
        complete: () => {
          console.log('Recipe creation process complete');
        }
      });
  }

  generateRecipe(): void {
    const token = localStorage.getItem('jwtToken');
    console.log(token);
    this.recipeService.generateRecipe(this.pet?.name, this.pet?.species, this.ingredients).subscribe({
      next: (response) => {
        console.log('Generated Recipe: ', response);
        const messageContent = response.choices[0].message.content;
        this.parseMarkdownResponse(messageContent);
        this.content = this.sanitizer.bypassSecurityTrustHtml(<string>marked(messageContent));
      },
      error: (error) => {
        console.error('Error generating a recipe: ', error);
      }
    })
  }

  async loadRecipes(): Promise<void> {
    this.recipeService.getRecipes(this.petId).subscribe({
      next: async recipes => {
        this.recipes = await Promise.all(
          recipes.map(async recipe => {
            const parsedTitle = await marked.parse(recipe.title);
            const parsedIngredients = await marked.parse(recipe.ingredients);
            const parsedInstructions = await marked.parse(recipe.instructions);
            return {
              id: recipe.id,
              title: this.sanitizer.bypassSecurityTrustHtml(parsedTitle),
              ingredients: this.sanitizer.bypassSecurityTrustHtml(parsedIngredients),
              instructions: this.sanitizer.bypassSecurityTrustHtml(parsedInstructions)
            };
          })
        );
        console.log('Recipes loaded');
      },
      error: error => {
        console.error('Error loading recipes:', error);
      }
    });
  }
  async searchByKeyword(keyword:string, petId:number | null):Promise<void>{

    this.recipeService.searchRecipe(keyword, petId).subscribe({
      next: response => {
        console.log("Successfully search recipes: ", response);
        this.recipesList = response.map(recipe => {
          const parsedTitle =  marked.parse(recipe.title);
          const parsedIngredients =  marked.parse(recipe.ingredients);
          const parsedInstructions =  marked.parse(recipe.instructions);

          return {
            id: recipe.id,
            title: this.sanitizer.bypassSecurityTrustHtml(<string>parsedTitle),
            ingredients: this.sanitizer.bypassSecurityTrustHtml(<string>parsedIngredients),
            instructions: this.sanitizer.bypassSecurityTrustHtml(<string>parsedInstructions)
          };
        })
      }, error: error => {
        console.error("Error searching recipes: ", error)
      }
    });
  }

  deleteRecipe(recipeId:number):void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    this.recipeService.deleteRecipe(recipeId).subscribe({
      next: response => {
        console.log("Recipe deleted successfully: ", response);
        window.location.reload();
      },
      error: error => {
        console.error("Error deleting recipe: ", error);
      }
    })
  }

  parseMarkdownResponse(markdown: string): void {
    const titleMarkdown = markdown.match(/### (.*?)\n/);
    const ingredientsMarkdown = markdown.match(/#### Ingredients:\n((?:- .*?\n)+)/);
    const instructionsMarkdown = markdown.match(/#### Instructions:\n([\s\S]*)/);

    if (titleMarkdown) {
      this.title = titleMarkdown[1];
    }
    if (ingredientsMarkdown) {
      this.ingredientsResponse = ingredientsMarkdown[1].trim();
    }
    if (instructionsMarkdown) {
      this.instructions = instructionsMarkdown[1].trim();
    }
  }
}
