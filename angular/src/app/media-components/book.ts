import { Component, inject, OnInit, resource, signal, ViewEncapsulation } from '@angular/core';
import { App } from '../app';

import { parseTar } from "nanotar";
import { Files } from '../fs/library-fs';
import { Item } from '../model/item';
import { Collection } from '../model/collection';
import { Library } from '../model/library';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'book',
  imports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatIconModule, FormsModule],
	host: {
		'[class.one-page]': '!app.widgets.book.thumbnailView() && app.widgets.book.pagesInDisplay() === 1',
		'[class.two-page]': '!app.widgets.book.thumbnailView() && app.widgets.book.pagesInDisplay() === 2',
	},
  template: `
	@if(bookCbt.hasValue()) {
		@if(!app.widgets.book.thumbnailView()) {
			@let pagesInDisplay = app.widgets.book.pagesInDisplay();
			@let leftPageNo = app.appState.currentPageSet() * pagesInDisplay;
			@let rightPageNo = leftPageNo + 1;

			@let showRightPage = pagesInDisplay === 2 && 
				bookCbt.value().length > rightPageNo;

			@if(bookCbt.value().length > leftPageNo) {
				<img #leftPage
					style="width: 100%"
					[src]="bookCbt.value()[leftPageNo].fullPage"
				/>
			}
			@if(showRightPage) {
				<img #rightPage
					style="width: 100%"
					[src]="bookCbt.value()[rightPageNo].fullPage"
				/>
			} 

		} @else {
			<div class="thumbnails">
		<!-- TODO add bookmark button to indicate this is a TOC target -->
				@for(page of bookCbt.value(); track $index) {
					<mat-card>
						<mat-card-header>
							<mat-card-title>{{page.name}}</mat-card-title>
							<mat-card-subtitle>{{$index}}
								<button matMiniFab > <!-- save ocr details -->
									<mat-icon fontSet="material-symbols-outlined">bookmark</mat-icon>
								</button>


							</mat-card-subtitle>
						</mat-card-header>
						<img mat-card-image
							[src]="page.thumbnail"
						/>
						<mat-card-actions>
							<mat-button-toggle-group name="page-type-{{$index}}" [(ngModel)]="page.type">
								<mat-button-toggle value="Text"> Text </mat-button-toggle>
								<mat-button-toggle value="Image"> Image </mat-button-toggle>
								<mat-button-toggle value="Blank"> Blank </mat-button-toggle>
								<mat-button-toggle value="Exclude"> Exclude </mat-button-toggle>
							</mat-button-toggle-group>
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
	appFs = inject(Files);

	bookCbt = resource<Page[], { library: Library | undefined, collection: Collection | undefined, item: Item | undefined }> ({
			params: () => ({
				library: this.app.resources.storedLibrary.value(), 
				collection: this.app.appState.currentCollection(), 
				item: this.app.appState.currentItem()
			}),
      loader: async ({ params, abortSignal }): Promise<Page[]> => {
        return new Promise(async (resolve, reject) => {
					const library = params.library;
					const cacheDirectory = library?.cacheDirectory?.handle as FileSystemDirectoryHandle;
					const collection = params.collection;
					const item = params.item;

					let pages: Page[] = [];
					let pagesByName: Record<string, Page> = {};

					const cachedFilename = item?.name + '.' + collection?.itemExtension;
					const cacheFileHandle = await cacheDirectory.getFileHandle(cachedFilename);
					const cacheFile = await cacheFileHandle.getFile();
					const cacheFileBuff = await cacheFile.arrayBuffer();
					const files = parseTar(cacheFileBuff, { metaOnly: false, });

					let { webpFiles, thumbnailFiles, mdFiles } = this.separateFiles(files);

					for(let i = 0; i < webpFiles.length; i++) {
						const file = webpFiles[i];
						const basename = file.name.replaceAll('.webp', '');
						let base64 = await this.appFs.bytesToBase64DataUrl(file.data);
						let page = {
							name: basename,
							fullPage: base64
						} as Page;

						if(item?.bookDetails?.blankPages?.includes(basename)) {
							page.type = 'Blank';
						} else if(item?.bookDetails?.imagePages?.includes(basename)) {
							page.type = 'Image';
						} else if(item?.bookDetails?.excludePages?.includes(basename)) {
							page.type = 'Exclude';
						} else {
							page.type = 'Text';
						}

						pages.push(page);
						pagesByName[basename] = page;
					}

					for(let i = 0; i < thumbnailFiles.length; i++) {
						const file = thumbnailFiles[i];
						let name = file.name.replace('.thumbnails/', '').replaceAll('.webp', '');
						let base64 = await this.appFs.bytesToBase64DataUrl(file.data);
						pagesByName[name].thumbnail = base64;
					}

					const decoder = new TextDecoder('utf-8')
					for(let i = 0; i < mdFiles.length; i++) {
						const file = mdFiles[i];
						let name = file.name.replace('.md/', '').replaceAll('.md', '');
						pagesByName[name].markdown = decoder.decode(file.data);
					}
					resolve(pages);
				});
      },
    });
		

	separateFiles(files: any[]) {
		let mdFiles = [];
		let thumbnailFiles = [];
		let webpFiles = [];

		for(let i = 0; i < files.length; i++) {
			const file = files[i];
			if(file.data) {
				if(file.name.startsWith('.md')) {
					mdFiles.push(file);
				} else if(file.name.startsWith('.thumbnails')) {
					thumbnailFiles.push(file);
				} else {
					webpFiles.push(file);
				}
			}
		}
		return {webpFiles, thumbnailFiles, mdFiles};
	}

}

export declare interface Page {
	name: string;
	markdown: string;
	thumbnail: string;
	fullPage: string;
	type: 'Text' | 'Image' | 'Blank' | 'Exclude' 
}