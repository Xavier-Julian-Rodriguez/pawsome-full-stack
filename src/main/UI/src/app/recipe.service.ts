import {Inject, Injectable, PLATFORM_ID} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Recipe} from "./recipe.model";
import {environment} from "../environments/environment.prod";

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: any) { }

  private apiUrl = `${environment.apiUrl}/api/recipes`;
  private petApiUrl = `${environment.apiUrl}/api/pets`;

  createRecipe(title: string, ingredients: string, instructions: string, petId: number): Observable<any> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    const body = {title, ingredients, instructions, petId};

    return this.http.post<any>(`${this.apiUrl}/my-pet-recipes`, body, {headers})
  }

  getPet(id: number): Observable<any> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(`${this.petApiUrl}/get-pet-details?petId=${id}`, { headers });
  }

  generateRecipe(pet_name: string | undefined, species: string | undefined, ingredients: string):Observable<any> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const body = { pet_name, species, ingredients};

    return this.http.post<any>(`${this.apiUrl}/generate-recipe`, body,{
      headers
    });
  }

  getRecipes(petId:number | null):Observable<Recipe[]> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const options = {
      headers: headers,
      params: {
        petId: petId?.toString() || ''
      }
    };

    return this.http.get<Recipe[]>(`${this.apiUrl}/list-pet-recipes`, options);
  }

  searchRecipe(keyword: string, petId: number | null): Observable<Recipe[]> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    let params = new HttpParams();
    params = params.append('keyword', keyword);
    // @ts-ignore
    params = params.append('petId', petId.toString());

    return this.http.get<Recipe[]>(`${this.apiUrl}/search`, { headers, params });
  }

  deleteRecipe(recipeId:number):Observable<any> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.delete(`${this.apiUrl}/delete-recipe/${recipeId}`, {headers})
  }
}
