import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookOptionsComponent } from './book-options.component';



@NgModule({
  declarations: [
    BookOptionsComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    BookOptionsComponent
  ]
})
export class BookOptionsModule { }
