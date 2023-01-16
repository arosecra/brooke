import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookComponent } from './book.component';
import { TopBarModule } from '../top-bar/top-bar.module';



@NgModule({
  declarations: [
    BookComponent
  ],
  imports: [
    CommonModule,
    TopBarModule
  ]
})
export class BookModule { }
