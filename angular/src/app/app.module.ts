import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule, } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { App } from './app.component';
import { BookComponent } from './book/book.component';
import { BrookeServerService } from './brookeserver.service';
import { TopBarComponent } from './top-bar/top-bar.component';
import { CollectionMenuComponent } from './collection-menu/collection-menu.component';
import { BrookeService } from './brooke.service';
import { ItemComponent } from './item/item.component';
import { JobDetailsComponent } from './job-details/job-details.component';
import { CategoryAndSeriesBrowserComponent } from './category-and-series-browser/category-and-series-browser.component';
import { PanelComponent } from './panel/panel.component';
import { BookOptionsComponent } from './book-options/book-options.component';
import { BookPageTurnerComponent } from './book-page-turner/book-page-turner.component';
import { BookTocComponent } from './book-toc/book-toc.component';
import { BreadcrumbComponent } from './breadcrumb/breadcrumb.component';
import { ModifyCollectionButtonComponent } from './modify-collection-button/modify-collection-button.component';
import { SeriesComponent } from './series/series.component';
import { ItemNamePipe } from "./brooke.pipe";

@NgModule({
    declarations: [App],
    providers: [
        BrookeService,
        BrookeServerService
    ],
    bootstrap: [App],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        ItemNamePipe,
        BookComponent,
        BookOptionsComponent,
        BookPageTurnerComponent,
        BookTocComponent,
        BreadcrumbComponent,
        CategoryAndSeriesBrowserComponent,
        CollectionMenuComponent,
        ItemComponent,
        JobDetailsComponent,
        ModifyCollectionButtonComponent,
        PanelComponent,
        SeriesComponent,
        TopBarComponent
    ]
})
export class AppModule { }
