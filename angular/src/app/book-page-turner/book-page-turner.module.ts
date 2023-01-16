import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookPageTurnerComponent } from './book-page-turner.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    BookPageTurnerComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
  ],
  exports: [
    BookPageTurnerComponent
  ]
})
export class BookPageTurnerModule { }
