import { Component, inject } from '@angular/core';
import { AppContextProvider } from '../../../providers/app-context-provider';

@Component({
  selector: 'book-toc',
  imports: [],
  template: ` TBD `,
  styles: ``,
})
//show the TOCs and the associated thumbnail
//   selecting a page forwards you to that
//
//how do i want to add a bookmark?
//    thought - show thumbnails for both pages visible
//            - if one is selected, prompt for toc item name
export class BookToCComponent {
  app = inject(AppContextProvider).app;
}
