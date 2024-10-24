import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-logout',
  standalone: true,
  imports: [],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.css'
})
export class LogoutComponent {

  constructor(private router: Router) {}
  logout(): void {
    localStorage.removeItem('jwtToken');
    this.router.navigate(['/login'])
  };
}
