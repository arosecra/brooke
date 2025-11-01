import { Component, inject, ViewEncapsulation } from '@angular/core';
import { App } from './app';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatChipAndRemove } from './media-components/mat-chip-and-remove';

@Component({
  selector: 'app-breadcrumb',
  imports: [MatIconModule, MatButtonModule, MatChipsModule, MatChipAndRemove],
  template: `
		@let appState = app.appState();
		@if(appState) {
			@if (appState.currentCollection()) {
				<mat-icon fontSet="material-symbols-outlined">chevron_right</mat-icon>
				<mat-chip-and-remove
					[label]="appState.currentCollection()?.name"
					(removed)="app.openHome()"
				/>
			}
			@if (appState.currentCategory()) {
				<mat-icon fontSet="material-symbols-outlined">chevron_right</mat-icon>
				<mat-chip-and-remove
					[label]="appState.currentCategory()?.name"
					(removed)="app.openCollection()"
				/>
			}
			@if (appState.currentSeries()) {
				<mat-icon fontSet="material-symbols-outlined">chevron_right</mat-icon>
				<mat-chip-and-remove
					[label]="appState.currentSeries()?.name"
					(removed)="app.openCategory()"
				/>
			}
			@if (appState.currentItem()) {
				<mat-icon fontSet="material-symbols-outlined">chevron_right</mat-icon>
				<mat-chip-and-remove
					[label]="appState.currentItem()?.name"
					(removed)="app.openCategory()"
				/>
			}
		}
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class AppBreadcrumb {
  app = inject(App);
}
