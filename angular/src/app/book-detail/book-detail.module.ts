import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookDetailComponent } from './book-detail.component';
import { TopBarModule } from '../top-bar/top-bar.module';
import { ItemModule } from '../item/item.module';
import { NgxFilesizeModule } from 'ngx-filesize';



@NgModule({
  declarations: [
    BookDetailComponent
  ],
  imports: [
    CommonModule,
		ItemModule,
		NgxFilesizeModule,
		TopBarModule,
  ],
  exports: [
    BookDetailComponent
  ]
})
export class BookDetailModule { }
