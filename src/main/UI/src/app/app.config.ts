import {ApplicationConfig, NgModule, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';

import { appRoutes } from './app.routes';
import {BrowserModule, provideClientHydration} from '@angular/platform-browser';
import {AppComponent} from "./app.component";
import {provideHttpClient, withFetch, withInterceptorsFromDi} from "@angular/common/http";
import {CommonModule} from "@angular/common";



export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(appRoutes),
    provideHttpClient(withFetch(), withInterceptorsFromDi()),
    provideClientHydration()]
};
