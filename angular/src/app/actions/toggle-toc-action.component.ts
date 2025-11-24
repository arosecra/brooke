import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../app.component';

@Component({
  selector: 'toggle-toc-action',
  imports: [MatButtonModule, MatIconModule],
  template: `
    <button
      matMiniFab
      title="Table of Contents"
      (click)="app.toggleToC()"
      [disabled]="!app.widgets().panel.showBook() || app.widgets().book.thumbnailView()"
    >
      <mat-icon fontSet="material-symbols-outlined">format_list_bulleted</mat-icon>
    </button>
  `,
  styles: ``,
})
export class ToggleToCComponent {
  app = inject(AppComponent);
}
