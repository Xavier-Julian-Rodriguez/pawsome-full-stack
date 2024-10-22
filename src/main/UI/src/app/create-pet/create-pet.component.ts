import {Component, OnInit} from '@angular/core';
import { PetService } from '../pet.service';
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {HttpHeaders} from "@angular/common/http";
import {SharedService} from "../shared.service";
import {Pet} from "../models/pet.model";
import {Router} from "@angular/router";
import {UtilsService} from "../utils.service";


@Component({
  selector: 'app-create-pet',
  templateUrl: './create-pet.component.html',
  imports: [FormsModule, NgIf, NgForOf],
  standalone: true,
  styleUrls: ['./create-pet.component.css']
})
export class CreatePetComponent implements OnInit{
  pets: Pet[] = [];
  name: string = '';
  age: number = 0;
  species: string = '';
  image: File | null = null;
  successMessage:string = '';
  errorMessage:string = '';

  constructor(private petService: PetService, private utilsService: UtilsService, private sharedService: SharedService, private router: Router) { }

  ngOnInit(): void {
    this.loadUserPets();
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.image = input.files[0];
    } else {
      this.image = null;
    }
  }

  createPet(): void {
    const token = localStorage.getItem('jwtToken');
    console.log('JWT Token:', token);

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    console.log('Headers:', headers);

    if (this.image) {
      this.petService.createPet(this.name, this.age, this.species, this.image).subscribe({
        next: (petId) => {
          this.sharedService.changePetId(petId);
          this.successMessage = petId.message;
          this.errorMessage = '';
          this.loadUserPets()
        },
        error: (error) => {
          console.error('Error creating pet', error);
          this.errorMessage = this.formatError(error);
          this.successMessage = '';
        },
        complete: () => {
          console.log('Pet creation process complete');
        }
      });
    } else {
      console.error('Image file is required');
      this.errorMessage = 'Image file is required';
    }
  }
  private formatError(error: any): string {
    if (error.error) {
      if (typeof error.error === 'string') {
        return error.error;
      } else if (typeof error.error === 'object') {
        return Object.values(error.error).join(' ');
      }
    }
    return 'An unexpected error occurred.';
  }
  updatePet(petId: number, pet: Pet) {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    this.petService.updatePet(petId, pet).subscribe(
      response => {
        console.log("Updated pet successfully", response);
        this.successMessage = response.message;
        this.errorMessage = '';
        this.router.navigate(["/pets"]);
      },
      error => {
        console.error("Error updating pet", error);
        this.errorMessage = this.formatError(error);
        this.successMessage = '';
      }
    )
  }

  deletePet(petId: number | null): void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    this.petService.deletePet(petId).subscribe((response) => {
      console.log("Pet deleted successfully", response);
      window.location.reload();
    }, error => {
      console.log("Error deleting pet: ", error);
    })
  };
  loadUserPets() {
    this.petService.getUserPets().subscribe({
      next: pets => {
        this.pets = pets.map(pet => {
          return {
            ...pet,
            image: pet.image ? `data:image/jpeg;base64,${this.utilsService.arrayBufferToBase64(pet.image)}` : null
          };
        });
        console.log('Pets loaded:', this.pets);
      },
      error: error => {
        console.log('Error loading pets:', error);
      }
    });
  }

  navigateToRecipePage(petId: number): void {
    this.router.navigate(["/my-pet-recipes", petId])
  }
}
