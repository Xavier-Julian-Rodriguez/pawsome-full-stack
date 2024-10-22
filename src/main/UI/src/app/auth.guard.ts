import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private userService: UserService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    const isAuthenticated = this.userService.isAuthenticated();
    console.log('AuthGuard - isAuthenticated:', isAuthenticated);
    if (isAuthenticated) {
      return true;
    } else {
      console.log('AuthGuard - Navigation to /login');
      this.router.navigate(['/login']);
      return false;
    }
  }
}
