import { Component, inject, ViewEncapsulation } from '@angular/core';
import { App } from './app';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatChipAndRemove } from './media-components/mat-chip-and-remove';

@Component({
  selector: 'breadcrumb',
  imports: [MatIconModule, MatButtonModule, MatChipsModule, MatChipAndRemove],
  template: `
    @if (app.appState.currentCollection()) {
      <mat-icon fontSet="material-symbols-outlined">chevron_right</mat-icon>
      <mat-chip-and-remove
        [label]="app.appState.currentCollection()?.name"
        (removed)="app.openHome()"
      />
    }
    @if (app.appState.currentCategory()) {
      <mat-icon fontSet="material-symbols-outlined">chevron_right</mat-icon>
      <mat-chip-and-remove
        [label]="app.appState.currentCategory()?.name"
        (removed)="app.openCollection()"
      />
    }
    @if (app.appState.currentSeries()) {
      <mat-icon fontSet="material-symbols-outlined">chevron_right</mat-icon>
      <mat-chip-and-remove
        [label]="app.appState.currentSeries()?.name"
        (removed)="app.openCategory()"
      />
    }
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class Breadcrumb {
  app = inject(App);
}
