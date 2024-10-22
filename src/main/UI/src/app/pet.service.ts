import {Inject, Injectable, PLATFORM_ID} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {isPlatformBrowser} from "@angular/common";
import {Pet} from "./models/pet.model";
import {environment} from "../environments/environment.prod";

@Injectable({
  providedIn: 'root'
})
export class PetService {
  private apiUrl = `${environment.apiUrl}/api/pets`;

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: any) { }

  createPet(name: string, age: number, species: string, image: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('name', name);
    formData.append('age', age.toString());
    formData.append('species', species);
    if (image) {
      formData.append('image', image, image.name);
    }

    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
    return this.http.post<any>(`${this.apiUrl}/pets`, formData, {headers})
  }

  getUserPets(): Observable<Pet[]> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<Pet[]>(`${this.apiUrl}/get-pets`, {headers});
  }

  updatePet(petId: number, pet: Pet): Observable<any> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });

    return this.http.put(`${this.apiUrl}/update-pet/${petId}`, pet, {headers});
  }

  deletePet(petId: number | null): Observable<any> {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.delete<any>(`${this.apiUrl}/delete-pet/${petId}`, {headers});
  }
}
