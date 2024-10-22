import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {NgFor, NgIf} from "@angular/common";
import {AppComponent} from "../app.component";
import {UserService} from "../user.service";
import {SharedService} from "../shared.service";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit{

  tokenValue:string | null = "";
  isAuthenticated: boolean = false;
  username:string | null = '';

  constructor(private router: Router, private appComponent: AppComponent, private sharedService: SharedService) {
  }

  ngOnInit():void {
    this.appComponent.isAuthenticated().subscribe(authStatus => {
      this.isAuthenticated = authStatus;
    });
    this.sharedService.currentUsername.subscribe(username => {
      this.username = username;
    })
  }

  extractToken():void {
    this.tokenValue = localStorage.getItem('jwtToken');
  }

  goToDashboard() {
    this.router.navigate([`/dashboard/${this.username}`]);
  }

  logout():void {
      localStorage.removeItem('jwtToken');
      this.appComponent.setAuthStateFalse();
      this.router.navigate(['/login']);
  }

  goToReports():void {
    this.router.navigate(["/reports"])
  }
  goToPets():void {
    this.router.navigate(["/pets"]);
  }

  goToRegister():void {
    this.router.navigate(["/register"])
  }

  goToLogin():void {
    this.router.navigate(["/login"])
  }

  goToHome():void {
    this.router.navigate(["/home"])
  }
}
