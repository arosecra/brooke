import { Component, inject, ViewEncapsulation } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../../app';

@Component({
  selector: 'book-toc',
  imports: [MatIconModule, MatButtonModule],
  template: `
    <button matMiniFab title="Table of Contents"> <!-- book toc -->
			<mat-icon fontSet="material-symbols-outlined">toc</mat-icon>
		</button>
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class BookToCComponent {
  app = inject(AppComponent);
}
