import { APP_INITIALIZER, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
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
import { BreadcrumbModule } from './breadcrumb/breadcrumb.module';
import { BookPageTurnerModule } from './book-page-turner/book-page-turner.module';
import { BookOptionsModule } from './book-options/book-options.module';
import { BookTocModule } from './book-toc/book-toc.module';
import { BookDetailModule } from './book-detail/book-detail.module';
import { MissingItemsModule } from './missing-items/missing-items.module';
import { CachingModule } from './caching/caching.module';
import { JobDetailsModule } from './job-details/job-details.module';
import { SynchronizeModule } from './synchronize/synchronize.module';
import { ModifyCollectionButtonModule } from './modify-collection-button/modify-collection-button.module';
import { ModifyCollectionModule } from './modify-collection/modify-collection.module';

import { brookeReducer, BrookeState } from './brooke.reducer';
import { Store, StoreModule } from '@ngrx/store';
import { getCollections } from './brooke.action';
import { BrookeEffect } from './brooke.effect';
import { EffectsModule } from '@ngrx/effects';

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
		BookDetailModule,
    BookOptionsModule,
    BookPageTurnerModule,
    BookTocModule,
    BreadcrumbModule,
		CachingModule,
    CategoryModule,
    CollectionModule,
    CollectionMenuModule,
    ConfigureModule,
    HomeModule,
		JobDetailsModule,
    ItemModule,
		MissingItemsModule,

		ModifyCollectionButtonModule,
		ModifyCollectionModule,

    SeriesModule,
		SynchronizeModule,
    TopBarModule,
    WidgetsModule,

    // StoreModule.forRoot({ brooke: brookeReducer }),
    // EffectsModule.forRoot([BrookeEffect]),
  ],
  providers: [
		BrookeService,
	
		// {
    //   provide: APP_INITIALIZER,
    //   useFactory: (store: Store<BrookeState>) => {
    //     return () => {
    //       store.dispatch(getCollections());
    //     };
    //   },
    //   multi: true,
    //   deps: [Store]
    // }

	],
  bootstrap: [AppComponent],
  exports: [
  ]
})
export class AppModule {
}
