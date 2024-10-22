import { Component } from '@angular/core';
import { User } from '../user';
import { UserService } from '../user.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {NgIf} from "@angular/common";
import {response} from "express";

@Component({
  selector: 'app-user-registration',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent {
  user: User = new User();
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private userService: UserService, private router: Router) { }

  registerUser() {
    this.userService.register(this.user).subscribe({
      next: response => {
        this.successMessage = response.message;
        this.errorMessage = '';
        this.router.navigate(['/login']).then(success => {
          if (success) {
            console.log('Navigation to login was successful');
          } else {
            console.log('Navigation to login failed');
          }
        });
      },
      error: error => {
        console.error('Error registering user', error);
        this.errorMessage = this.formatError(error);
        this.successMessage = '';
      }
    });
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
}
