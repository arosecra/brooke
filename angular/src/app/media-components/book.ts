import { Component, inject, OnInit, resource, signal, ViewEncapsulation } from '@angular/core';
import { App } from '../app';

import { parseTar } from "nanotar";
import { Files } from '../fs/library-fs';
import { Item } from '../model/item';
import { Collection } from '../model/collection';
import { Library } from '../model/library';


@Component({
  selector: 'book',
  imports: [],
	host: {
		'[class.one-page]': 'app.widgets.book.pagesInDisplay() === 1',
		'[class.two-page]': 'app.widgets.book.pagesInDisplay() === 2',
	},
  template: `
	@if(bookCbt.hasValue()) {
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
						let base64 = await this.appFs.bytesToBase64DataUrl(file.data);
						let page = {
							fullPage: base64
						} as Page;
						pages.push(page);
						pagesByName[file.name.replaceAll('.webp', '')] = page;
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
	markdown: string;
	thumbnail: string;
	fullPage: string;
}