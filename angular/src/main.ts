import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';


import { importProvidersFrom } from '@angular/core';
import { App } from './app/app.component';
import { withInterceptorsFromDi, provideHttpClient } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { BrowserModule, bootstrapApplication } from '@angular/platform-browser';
import { BrookeServerService } from './app/brookeserver.service';
import { BrookeService } from './app/brooke.service';


bootstrapApplication(App, {
    providers: [
        importProvidersFrom(BrowserModule),
        BrookeService,
        BrookeServerService,
        provideAnimations(),
        provideHttpClient(withInterceptorsFromDi())
    ]
})
  .catch(err => console.error(err));
