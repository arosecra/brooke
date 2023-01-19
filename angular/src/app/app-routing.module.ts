import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookDetailComponent } from './book-detail/book-detail.component';
import { BookComponent } from './book/book.component';
import { CachingComponent } from './caching/caching.component';
import { CategoryComponent } from './category/category.component';
import { CollectionComponent } from './collection/collection.component';
import { HomeComponent } from './home/home.component';
import { JobDetailsComponent } from './job-details/job-details.component';
import { MissingItemsComponent } from './missing-items/missing-items.component';
import { ModifyCollectionComponent } from './modify-collection/modify-collection.component';
import { SeriesComponent } from './series/series.component';
import { SynchronizeComponent } from './synchronize/synchronize.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'book', component: BookComponent },
  { path: 'book-detail', component: BookDetailComponent },
  { path: 'cache', component: CachingComponent },
  { path: 'collection', component: CollectionComponent },
  { path: 'category', component: CategoryComponent },
  { path: 'job-details', component: JobDetailsComponent },
	{ path: 'missing-items', component: MissingItemsComponent },
	{ path: 'modify-collection', component: ModifyCollectionComponent },
  { path: 'series', component: SeriesComponent },
  { path: 'synchronize', component: SynchronizeComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
