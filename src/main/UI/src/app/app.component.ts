import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router, RouterOutlet} from '@angular/router';
import {NavbarComponent} from "./navbar/navbar.component";
import {FooterComponent} from "./footer/footer.component";
import {BehaviorSubject, Observable} from "rxjs";
import {DisclaimerComponent} from "./disclaimer/disclaimer.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, FooterComponent, DisclaimerComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'PAWSOME';
  username:string | null = "";
  private authState = new BehaviorSubject<boolean>(this.hasToken());

  ngOnInit() {
    this.extractUsername();
  }

  constructor(private route: ActivatedRoute) {
  }


  extractUsername():void {
    this.username = this.route.snapshot.paramMap.get('usernameResponse');
  }
  isAuthenticated(): Observable<boolean> {
    return this.authState.asObservable();
  }

  private hasToken(): boolean {
    if (typeof localStorage !== 'undefined') {
      const token = localStorage.getItem('jwtToken');
      return token !== null && token !== '';
    }
    return false;
  }

  getToken(): string | null {
    if (typeof localStorage !== 'undefined') {
      return localStorage.getItem('jwtToken');
    }
    return null;
  }

  setAuthStateTrue(token: string): void {
      this.authState.next(true);
  }

  setToken(token: string): void {
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem('jwtToken', token);
      this.authState.next(true);
    }
  }

  setAuthStateFalse(): void {
      this.authState.next(false);
  }
}
