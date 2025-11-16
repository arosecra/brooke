import { Component, inject, Injectable, ViewEncapsulation } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from './app';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AppBreadcrumbComponent } from './app-breadcrumb';
import { MatPaginatorIntl, MatPaginatorModule } from '@angular/material/paginator';
import { BookToCComponent } from './media-components/book-toc';

@Injectable()
export class CustomPaginatorIntl extends MatPaginatorIntl {

  override getRangeLabel = (page: number, pageSize: number, length: number): string => {
    if (length === 0) {
      return `Page 1 of 1`;
    }
    return `Page ${page * pageSize + 1} of ${length}`;
  }
}

@Component({
  selector: 'app-header',
  imports: [
		MatButtonModule, MatIconModule, MatToolbarModule, MatPaginatorModule,
		BookToCComponent, AppBreadcrumbComponent, 
	],
	providers: [
		{provide: MatPaginatorIntl, useClass: CustomPaginatorIntl }
	],
  template: `	
@let widgets = app.widgets();
@let appState = app.appState();
@let resources = app.resources();
@if(widgets && appState && resources) {
<mat-toolbar color="primary" class="flex flex-align-center">
	<mat-toolbar-row>
		<button matMiniFab >
			<mat-icon fontSet="material-symbols-outlined">newsstand</mat-icon>
		</button>
		<h1>Brooke</h1>
		<app-breadcrumb class="flex flex-align-center flex-no-gap"></app-breadcrumb>
		<span class="spacer"></span>
		
		@if (!widgets.panel.showBook()) {
			<button matMiniFab (click)="appState.showLibraryEditorManual.set(true)" title="Modify Categories">
				<mat-icon fontSet="material-symbols-outlined">library_books</mat-icon>
			</button>
			<button matMiniFab title="Write Categories">
				<mat-icon fontSet="material-symbols-outlined">sync_arrow_up</mat-icon>
			</button>
			<button matMiniFab (click)="appState.showSettingsManual.set(true)">
				<mat-icon fontSet="material-symbols-outlined">settings</mat-icon>
			</button>
		}
		
		@if (widgets.panel.showBook()) {

			<button matMiniFab (click)="app.toggleFullScreen()" title="Fullscreen">
				<mat-icon fontSet="material-symbols-outlined">
					@if(widgets.fullscreen()) { fullscreen_exit } 
					@else { fullscreen }
				</mat-icon>
			</button>

			<button matMiniFab (click)="app.stopTextToSpeech()" title="Stop Text to Speech">
				<mat-icon fontSet="material-symbols-outlined">stop</mat-icon>
			</button>
			<button matMiniFab (click)="app.textToSpeech()" title="Text to Speech">
				<mat-icon fontSet="material-symbols-outlined">text_to_speech</mat-icon>
			</button>
			<button matMiniFab (click)="app.toggleMarkdownView()" title="View Markdown">
				<mat-icon fontSet="material-symbols-outlined">markdown</mat-icon>
			</button>
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
			<button matMiniFab (click)="app.toggleSideBySide()" title="Compare Markdown and Image">
				<mat-icon fontSet="material-symbols-outlined">compare</mat-icon>
			</button>
			@if (resources.bookCbt.hasValue()) {
				<mat-paginator
					(page)="app.handlePaginationEvent($event)"
					[length]="resources.bookCbt.value().length"
					[pageSize]="widgets.book.pagesInDisplay()"
					[disabled]="false"
					[showFirstLastButtons]="false"
					[pageIndex]="appState.currentPageSet()"
					[hidePageSize]="true"
					aria-label="Select page"
				>
				</mat-paginator>
			}
		}
	</mat-toolbar-row>
	@if(widgets.book.thumbnailView()) {
		<mat-toolbar-row>
  		<span class="spacer"></span>
			
			<button matMiniFab (click)="app.updateItem()" title="Save" > <!-- save ocr details -->
				<mat-icon fontSet="material-symbols-outlined">save</mat-icon>
			</button>
		</mat-toolbar-row>
	}
	</mat-toolbar>
		}
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class AppHeaderComponent {
  app = inject(AppComponent);  
}