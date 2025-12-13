import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../app.component';

@Component({
  selector: 'next-page-action',
  imports: [MatButtonModule, MatIconModule],
  template: `
    <button matMiniFab (click)="app.goToNextPage()" title="Next Page">
      <mat-icon fontSet="material-symbols-outlined">chevron_right</mat-icon>
    </button>
  `,
  styles: ``,
})
export class NextPageActionComponent {
  app = inject(AppComponent);

}
