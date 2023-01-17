import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookDetailComponent } from './book-detail/book-detail.component';
import { BookComponent } from './book/book.component';
import { CachingComponent } from './caching/caching.component';
import { CategoryComponent } from './category/category.component';
import { CollectionComponent } from './collection/collection.component';
import { HomeComponent } from './home/home.component';
import { SeriesComponent } from './series/series.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'book', component: BookComponent },
  { path: 'book-detail', component: BookDetailComponent },
  { path: 'cache', component: CachingComponent },
  { path: 'collection', component: CollectionComponent },
  { path: 'category', component: CategoryComponent },
  { path: 'series', component: SeriesComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
