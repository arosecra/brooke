import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../app.component';

@Component({
  selector: 'previous-page-action',
  imports: [MatButtonModule, MatIconModule],
  template: `
    <button matMiniFab (click)="app.goToPreviousPage()" title="Previous Page">
      <mat-icon fontSet="material-symbols-outlined">chevron_left</mat-icon>
    </button>
  `,
  styles: ``,
})
export class PreviousPageActionComponent {
  app = inject(AppComponent);
}
