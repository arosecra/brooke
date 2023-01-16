import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { AdminModule } from './admin/admin.module';
import { BookModule } from './book/book.module';
import { CategoryModule } from './category/category.module';
import { CollectionModule } from './collection/collection.module';
import { ConfigureModule } from './configure/configure.module';
import { HomeModule } from './home/home.module';
import { SeriesModule } from './series/series.module';
import { WidgetsModule } from './widgets/widgets.module';
import { BrookeService } from './brooke.service';
import { HttpClientModule } from '@angular/common/http';
import { ItemModule } from './item/item.module';
import { TopBarModule } from './top-bar/top-bar.module';
import { CollectionMenuModule } from './collection-menu/collection-menu.module';
import { CategoryMenuModule } from './category-menu/category-menu.module';
import { BreadcrumbComponent } from './breadcrumb/breadcrumb.component';
import { BreadcrumbModule } from './breadcrumb/breadcrumb.module';
import { BookPageTurnerModule } from './book-page-turner/book-page-turner.module';
import { CachingModule } from './caching/caching.module';
import { BookOptionsModule } from './book-options/book-options.module';
import { BookTocComponent } from './book-toc/book-toc.component';
import { BookTocModule } from './book-toc/book-toc.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule, 

    AdminModule,
    BookModule,
    BookOptionsModule,
    BookPageTurnerModule,
    BookTocModule,
    BreadcrumbModule,
    CachingModule,
    CategoryMenuModule,
    CategoryModule,
    CollectionModule,
    CollectionMenuModule,
    ConfigureModule,
    HomeModule,
    ItemModule,
    MaterialModule,
    SeriesModule,
    TopBarModule,
    WidgetsModule,

    
  ],
  providers: [BrookeService],
  bootstrap: [AppComponent],
  exports: [
  ]
})
export class AppModule { }
