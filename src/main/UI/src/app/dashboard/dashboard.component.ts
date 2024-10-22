import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {LogoutComponent} from "../logout/logout.component";
import {HttpHeaders} from "@angular/common/http";
import {UserService} from "../user.service";
import {NgFor, NgIf} from "@angular/common";
import {FormsModule, NgForm} from "@angular/forms";
import {User} from "../user";
import {SharedService} from "../shared.service";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    LogoutComponent,
    NgIf,
    FormsModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{
  username:string | null = "";
  user_id:number = 0;
  firstName:string = "";
  lastName:string = "";
  new_password:string = "";
  new_username:string = "";
  tokenValue:string | null = "";
  errorMessage:string = '';
  successMessage:string = '';
  nav_username:string | null = '';

  constructor(private router: Router, private userService: UserService, private route: ActivatedRoute, private sharedService: SharedService) {
  }

  ngOnInit():void {
    this.extractUsername();
    if(this.username != null) {
      this.getUserDetails();
    }
    this.showUpdateUserForm();
    this.extractToken();
  }

  extractUsername():void {
    this.username = this.route.snapshot.paramMap.get('usernameResponse');
  }
  extractToken():void {
    this.tokenValue = localStorage.getItem('jwtToken');
  }

  getUserDetails():void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    this.userService.getUserByUsername(this.username).subscribe({
      next: response => {
        if(response) {
          this.user_id = response.id;
        }
      }
    })
  }

  showUpdateUserForm():void {
    const updateButton = document.getElementById("update-user-button");
    const cancelButton = document.getElementById("cancel-update-user-button")
    if (updateButton) {
    updateButton.addEventListener("click", () => {
    const updateFormElement = document.getElementById("update-user-form");
    if (updateFormElement) {
      updateFormElement.style.display = "block";
      updateButton.style.display = "none";
    }
    if (cancelButton) {
      cancelButton.style.display = "block";
    }
    })
    }
  }

  hideUpdateUserForm():void {
    const cancelButton = document.getElementById("cancel-update-user-button")
    const updateButton = document.getElementById("update-user-button");
    const updateFormElement = document.getElementById("update-user-form");

    if (updateFormElement) {
      updateFormElement.style.display = "none";
      if (updateButton) {
        updateButton.style.display = "block";
      }
    }
    if (cancelButton) {
      cancelButton.style.display = "none";
    }
  }
  NavigateToCreatePetPage() {
    this.router.navigate(['/pets'])
  }

  UpdateUser(userId:number, user: User):void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });
    this.userService.updateUser(this.user_id, user).subscribe({
      next: response => {
        this.successMessage = response.message;
        this.errorMessage = '';
        this.username = this.new_username;
        this.nav_username = this.username;
        this.sharedService.changeUsername(this.nav_username);
        const newToken = response.token;
        localStorage.setItem('jwtToken', newToken);

        this.router.navigate([`/dashboard/${this.username}`]);

      },
      error: error => {
        console.error("Error updating user: ", error);
        this.errorMessage = this.formatError(error);
        this.successMessage = '';
      }
    })
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

  DeleteUser():void {
    const token = localStorage.getItem('jwtToken');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
    });

    this.userService.deleteUser(this.user_id).subscribe({
      next: (response) => {
        console.log("Successfully deleted user: ", response);
        this.router.navigate(["/login"]);
      }, error: error => {
        console.log("Error deleting user: ", error)
    }
    })
  }
}
