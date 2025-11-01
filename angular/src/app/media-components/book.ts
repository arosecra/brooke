import { Component, inject, ViewEncapsulation } from '@angular/core';
import { App } from '../app';

import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'book',
  imports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatIconModule, FormsModule],
	host: {
		'[class.one-page]': '!app.widgets()?.book?.thumbnailView() && app.widgets()?.book?.pagesInDisplay() === 1',
		'[class.two-page]': '!app.widgets()?.book?.thumbnailView() && app.widgets()?.book?.pagesInDisplay() === 2',
	},
  template: `
	@let widgets = app.widgets();
	@let appState = app.appState();
	@let resources = app.resources();
	@let bookCbtResource = resources?.bookCbt;
	@if(appState && widgets && resources && bookCbtResource && bookCbtResource.hasValue()) {
		@let bookCbt = bookCbtResource.value();
		@if(!widgets.book.thumbnailView()) {
			@let pagesInDisplay = widgets.book.pagesInDisplay();
			@let leftPageNo = appState.currentPageSet() * pagesInDisplay;
			@let rightPageNo = leftPageNo + 1;

			@let showRightPage = pagesInDisplay === 2 && 
				bookCbt.length > rightPageNo;

			@if(bookCbt.length > leftPageNo) {
				<img #leftPage
					style="width: 100%"
					[src]="bookCbt[leftPageNo].fullPage"
				/>
			}
			@if(showRightPage) {
				<img #rightPage
					style="width: 100%"
					[src]="bookCbt[rightPageNo].fullPage"
				/>
			} 

		} @else {
			<div class="thumbnails">
		<!-- TODO add bookmark button to indicate this is a TOC target -->
				@for(page of bookCbt; track $index) {
					<mat-card>
						<mat-card-header>
							<!-- <mat-card-title>{{page.name.replaceAll(appState.currentItem()!.name, '').replaceAll('-', ' ')}}</mat-card-title>
							<mat-card-subtitle>{{$index}}
								<button matMiniFab > 
									<mat-icon fontSet="material-symbols-outlined">bookmark</mat-icon>
								</button>
							</mat-card-subtitle> -->
							<mat-card-title>
							</mat-card-title>
						</mat-card-header>
						<img mat-card-image
							style="width: 250px"
							[src]="page.thumbnail"
						/>
						<mat-card-actions>
							<div>
								<button matMiniFab > 
									<mat-icon fontSet="material-symbols-outlined">bookmark</mat-icon>
								</button>
							</div>
							<div>
								<mat-button-toggle-group name="page-type-{{$index}}" [(ngModel)]="page.type">
									<mat-button-toggle value="Text"> Text </mat-button-toggle>
									<mat-button-toggle value="Image"> Image </mat-button-toggle>
									<mat-button-toggle value="Blank"> Blank </mat-button-toggle>
									<mat-button-toggle value="Exclude"> Exclude </mat-button-toggle>
								</mat-button-toggle-group>
							</div>
						</mat-card-actions>
					</mat-card>
				}
			</div>
		}

	} @else {
		<div>Loading...</div>
	}
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class Book {
  app = inject(App);
}

