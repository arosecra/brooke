import { Component, inject, ViewEncapsulation } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { App } from './app';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AppBreadcrumb } from './app-breadcrumb';
import { MatPaginatorModule } from '@angular/material/paginator';
import { BookToC } from './media-components/book-toc';

@Component({
  selector: 'app-header',
  imports: [
		MatButtonModule, MatIconModule, MatToolbarModule, MatPaginatorModule,
		BookToC, AppBreadcrumb, 
	],
  template: `
<mat-toolbar color="primary" class="row-flex row-flex-align-center">
	<mat-toolbar-row>
  <button matMiniFab >
    <mat-icon fontSet="material-symbols-outlined">newsstand</mat-icon>
  </button>
  <h1>Brooke</h1>
  <app-breadcrumb class="row-flex row-flex-align-center"></app-breadcrumb>
  <span class="spacer"></span>
  
	@if (!app.widgets.panel.showBook() && !app.widgets.panel.showSeries()) {
		<button matMiniFab (click)="app.appState.showLibraryEditorManual.set(true)" title="Modify Categories">
			<mat-icon fontSet="material-symbols-outlined">library_books</mat-icon>
		</button>
		<button matMiniFab title="Write Categories">
			<mat-icon fontSet="material-symbols-outlined">sync_arrow_up</mat-icon>
		</button>
	}
  
  @if (app.widgets.panel.showBook()) {
		<button matMiniFab (click)="app.toggleThumbnailView()" title="Thumbnails"> <!-- ocr details / thumbnail view -->
			<mat-icon fontSet="material-symbols-outlined">dataset</mat-icon>
		</button>
		<button matMiniFab title="Book Options"> <!-- book options -->
			<mat-icon fontSet="material-symbols-outlined">book_2</mat-icon>
		</button>
		<button matMiniFab title="Right to Left Mode"> <!-- read in reverse -->
			<mat-icon fontSet="material-symbols-outlined">rotate_left</mat-icon>
		</button>
		<book-toc/>
		<button matMiniFab (click)="app.toggleOneOrTwoPageMode()" title="Toggle Page Mode">
			<mat-icon fontSet="material-symbols-outlined">two_pager</mat-icon>
		</button>
		<button matMiniFab title="Compare Markdown and Image">
			<mat-icon fontSet="material-symbols-outlined">compare</mat-icon>
		</button>
    <mat-paginator
      (page)="app.handlePaginationEvent($event)"
      [length]="app.appState.currentItem()?.bookDetails?.numberOfPages"
      [pageSize]="app.widgets.book.pagesInDisplay()"
      [disabled]="false"
      [showFirstLastButtons]="true"
			[pageIndex]="app.appState.currentPageSet()"
      [hidePageSize]="true"
      aria-label="Select page"
    >
    </mat-paginator>
  }
	<button matMiniFab (click)="app.appState.showSettingsManual.set(true)">
    <mat-icon fontSet="material-symbols-outlined">settings</mat-icon>
  </button>
	</mat-toolbar-row>
	@if(app.widgets.book.thumbnailView()) {
		<mat-toolbar-row>
  		<span class="spacer"></span>
			
			<button matMiniFab (click)="app.updateItem()" title="Save" > <!-- save ocr details -->
				<mat-icon fontSet="material-symbols-outlined">save</mat-icon>
			</button>
		</mat-toolbar-row>
	}
</mat-toolbar>
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class AppHeader {
  app = inject(App);
  
}
