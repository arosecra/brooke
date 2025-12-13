import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../../app.component';

@Component({
  selector: 'book-toc',
  imports: [MatIconModule, MatButtonModule],
  template: `
    <button matMiniFab title="Table of Contents"> <!-- book toc -->
			<mat-icon fontSet="material-symbols-outlined">toc</mat-icon>
		</button>
  `,
  styles: ``,

})
//show the TOCs and the associated thumbnail
//   selecting a page forwards you to that
//   
//how do i want to add a bookmark?
//    thought - show thumbnails for both pages visible
//            - if one is selected, prompt for toc item name
export class BookToCComponent {
  app = inject(AppComponent);
}
