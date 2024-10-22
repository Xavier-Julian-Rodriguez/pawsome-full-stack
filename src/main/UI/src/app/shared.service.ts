import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  private petIdSource = new BehaviorSubject<number | null>(null);
  currentPetId = this.petIdSource.asObservable();

  changePetId(petId: number) {
    this.petIdSource.next(petId);
  }

  private usernameSource = new BehaviorSubject<string | null>(null);
  currentUsername = this.usernameSource.asObservable();

  changeUsername(username: string) {
    this.usernameSource.next(username);
  }
}
