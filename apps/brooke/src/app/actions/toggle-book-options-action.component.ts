import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../app.component';

@Component({
  selector: 'toggle-book-options-action',
  imports: [MatButtonModule, MatIconModule],
  template: `
    <button
      matMiniFab
      title="Book Options"
      (click)="app.toggleBookOptions()"
      [disabled]="!app.widgets().panel.showBook() || app.widgets().book.thumbnailView()"
    >
      <mat-icon fontSet="material-symbols-outlined">book_2</mat-icon>
    </button>
  `,
  styles: ``,
})
export class ToggleBookOptionsComponent {
  app = inject(AppComponent);
}
