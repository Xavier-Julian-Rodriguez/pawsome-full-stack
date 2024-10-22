import { Routes } from '@angular/router';
import {UserRegistrationComponent} from "./user-registration/user-registration.component";
import {LoginComponent} from "./login/login.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {AuthGuard} from "./auth.guard";
import {CreatePetComponent} from "./create-pet/create-pet.component";
import {RecipeComponent} from "./recipe/recipe.component";
import {HomeComponent} from "./home/home.component";
import {ReportsComponent} from "./reports/reports.component";

export const appRoutes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'register', component: UserRegistrationComponent },
  {path: 'login', component: LoginComponent},
  {path: 'dashboard/:usernameResponse', component: DashboardComponent, canActivate: [AuthGuard]},
  {path: 'pets', component: CreatePetComponent, canActivate: [AuthGuard]},
  {path: 'reports', component: ReportsComponent, canActivate: [AuthGuard]},
  {path: 'my-pet-recipes/:petId', component: RecipeComponent, canActivate: [AuthGuard]},
  {path: '**', redirectTo: '/login'}
];
