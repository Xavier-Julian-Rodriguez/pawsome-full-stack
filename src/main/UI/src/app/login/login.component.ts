import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import {AppComponent} from "../app.component";
import {SharedService} from "../shared.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';
  successMessage:string = '';
  nav_username:string= '';

  constructor(private userService: UserService, private router: Router, private appComponent: AppComponent, private sharedService: SharedService,
  ) { }

  login() {
    this.userService.login(this.username, this.password).subscribe({
      next: response => {
        this.successMessage = response.message;
        this.errorMessage = '';
        const usernameResponse = response.username;
        this.username = response.username;
        this.appComponent.setToken(response.token);
        this.nav_username = response.username;
        this.sharedService.changeUsername(this.nav_username);
        this.router.navigate(['/dashboard', usernameResponse]).then(success => {
          if (success) {
            console.log('Navigation to dashboard was successful');
          } else {
            console.log('Navigation to dashboard failed');
          }
        }).catch(error => {
          console.error('Navigation error:', error);
          this.errorMessage = this.formatError(error);
          this.successMessage = '';
        });
      },
      error: error => {
        console.error('Error logging in', error);
        this.errorMessage = 'Invalid username or password';
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
