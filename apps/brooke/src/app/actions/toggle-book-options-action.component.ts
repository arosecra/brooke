import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppContextProvider } from '../../providers/app-context-provider';

@Component({
  selector: 'toggle-book-options-action',
  imports: [MatButtonModule, MatIconModule],
  template: `
    <button
      matMiniFab
      title="Book Options"
      (click)="app.openBookOptions()"
      [disabled]="app.bookViewMode() === 'IMAGE'"
    >
      <mat-icon fontSet="material-symbols-outlined">book_2</mat-icon>
    </button>
  `,
  styles: ``,
})
export class ToggleBookOptionsComponent {
  app = inject(AppContextProvider).app;
}
