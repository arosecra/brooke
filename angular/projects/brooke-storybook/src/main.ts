
import { BrowserModule, bootstrapApplication } from '@angular/platform-browser';
import { Routes, provideRouter, withComponentInputBinding } from '@angular/router';
import { AppComponent } from './app/app.component';
import { importProvidersFrom } from '@angular/core';
import { HomeComponent } from './app/home/home.component';
import { StorySectionComponent } from './app/story-section/story-section.component';
import { SDSCIconsComponent } from './app/s-dsc-icons/s-dsc-icons.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
	{ 
		path: 'story-section/Design System Components/Icons', 
		component: SDSCIconsComponent 
	},
	{ 
		path: 'story-section/:sectionName/:subSectionName', 
		component: StorySectionComponent 
	},
]

bootstrapApplication(AppComponent, {
  providers: [
		importProvidersFrom([BrowserModule]),
		provideRouter(routes, withComponentInputBinding()),
		
	],
})
  .catch(err => console.error(err));
