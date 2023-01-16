import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookTocComponent } from './book-toc.component';



@NgModule({
  declarations: [
    BookTocComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    BookTocComponent
  ]
})
export class BookTocModule { }
