import {
  ApplicationConfig,
  importProvidersFrom,
  provideExperimentalZonelessChangeDetection,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { BrowserModule } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { BrookeServerService } from './brookeserver.service';
import { BrookeService } from './brooke.service';

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(BrowserModule),
    BrookeService,
    BrookeServerService,
    provideAnimations(),
    provideHttpClient(withInterceptorsFromDi()),

    // provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),

		provideExperimentalZonelessChangeDetection(),
  ],
};
