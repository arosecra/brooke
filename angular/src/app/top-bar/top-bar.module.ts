import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TopBarComponent } from './top-bar.component';
import { BreadcrumbModule } from '../breadcrumb/breadcrumb.module';
import { BookPageTurnerModule } from '../book-page-turner/book-page-turner.module';
import { BookOptionsModule } from '../book-options/book-options.module';
import { BookTocModule } from '../book-toc/book-toc.module';
import { ModifyCollectionButtonModule } from '../modify-collection-button/modify-collection-button.module';



@NgModule({
  declarations: [
    TopBarComponent
  ],
  imports: [
    CommonModule,
    BreadcrumbModule,
    BookOptionsModule,
    BookPageTurnerModule,
    BookTocModule,
		ModifyCollectionButtonModule
  ],
  exports: [
    TopBarComponent
  ]
})
export class TopBarModule { }
