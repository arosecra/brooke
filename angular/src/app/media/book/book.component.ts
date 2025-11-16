import { Component, inject, ViewEncapsulation } from '@angular/core';
import { AppComponent } from '../../app';

import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Page, PageType } from '../../model/page';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { PageComponent } from '../page/page.component';

@Component({
  selector: 'book',
  imports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatIconModule, FormsModule, MatFormFieldModule, MatInputModule, PageComponent],
	host: {
		'[class.one-page]': '!app.widgets()?.book?.thumbnailView() && app.widgets()?.book?.pagesInDisplay() === 1 && !app.widgets()?.book?.sideBySide()',
		'[class.two-page]': '!app.widgets()?.book?.thumbnailView() && (app.widgets()?.book?.pagesInDisplay() === 2 || app.widgets()?.book?.sideBySide())',
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

			@let showRightPage = widgets.book.sideBySide() || (pagesInDisplay === 2 && bookCbt.length > rightPageNo);

			@if(bookCbt.length > leftPageNo) {
				<page [page]="bookCbt[leftPageNo]" 
					[preferMarkdown]="widgets.book.markdownView() || widgets.book.sideBySide()"
				/>
			} 
			@if(showRightPage) {
				<page [page]="widgets.book.sideBySide() ? bookCbt[leftPageNo] : bookCbt[rightPageNo]" 
					[preferMarkdown]="widgets.book.markdownView()"
				/>
			} 

		} @else {
			<div class="thumbnails">
		<!-- TODO add bookmark button to indicate this is a TOC target -->
				@for(page of bookCbt; track $index) {
					<mat-card>
						<img mat-card-image 
							style="width: 250px" 
							[src]="page.thumbnail" 
							(click)="cycleImageType(page)"
						/>
						<mat-card-actions>
							<div>
								<!-- <button matMiniFab > 
									<mat-icon fontSet="material-symbols-outlined" >bookmark</mat-icon>
								</button> -->
								  <mat-form-field hintLabel="Max 30 characters" >
										<mat-label>Chapter</mat-label>
										<input matInput #input maxlength="30" placeholder="Chapter" [(ngModel)]="page.bookmarkName" name="bookmarkName-{{$index}}" />
										<mat-hint align="end">{{page.bookmarkName?.length ?? 0}}/30</mat-hint>
									</mat-form-field>
							</div>
							<div>
								<mat-button-toggle-group name="page-type-{{$index}}" [(ngModel)]="page.type">
									<mat-button-toggle value="Text"> Text </mat-button-toggle>
									<mat-button-toggle value="Image"> Image </mat-button-toggle>
									<mat-button-toggle value="Blank"> Blank </mat-button-toggle>
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
export class BookComponent {
  app = inject(AppComponent);

	cycleImageType(page: Page) {
		const types: PageType[] = ['Text', 'Image', 'Blank'];
		let idx = types.findIndex((type) => page.type === type);
		idx = (idx + 1) % types.length;
		page.type = types[idx];
	}
}

