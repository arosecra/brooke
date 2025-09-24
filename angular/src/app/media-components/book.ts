import { Component, effect, inject, OnInit, signal, viewChild, ViewEncapsulation } from '@angular/core';
import { App } from '../app';

import {
	parseTar
} from "nanotar";
import { Files } from '../fs/library-fs';


//todo
//  keep a array of img elements. say 20
//  keep another array that is the length of the book
//  currentLeftPage => index[currentLeftPage] gives a # into the img buffer
//  

@Component({
  selector: 'book',
  imports: [],
  template: `
	@if(bookIsLoaded()) {
		@let leftPageNo = app.appState.currentPageSet() * 2;
		@let rightPageNo = app.appState.currentPageSet() * 2 + 1;
		@if(pages.length > leftPageNo) {
			<img #leftPage
				style="width: 100%"
				[src]="pages[leftPageNo]"
			/>
		}
		@if(pages.length > rightPageNo) {
			<img #rightPage
				style="width: 100%"
				[src]="pages[rightPageNo]"
			/>
		}

	} @else {
		<div>Loading...</div>
	}
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class Book implements OnInit {
  app = inject(App);
	appFs = inject(Files);

	bookIsLoaded = signal<boolean>(false);

	test = signal<string>('');

	pages: string[] = [];

	ngOnInit() {
		let cacheDirectory = this.app.resources.storedLibrary.value()?.settingsByName['cacheDirectory'].value as FileSystemDirectoryHandle;
		// const extract = tarStream.extract();
		const collection = this.app.appState.currentCollection();
		const item = this.app.appState.currentItem();

		cacheDirectory.getFileHandle(item?.name + '.' + collection?.itemExtension).then((cachedFileHandle) => {
			cachedFileHandle.getFile().then((cachedFile) => {
				cachedFile.arrayBuffer().then((buff) => {
					const files = parseTar(buff, {
						metaOnly: false,
					});
					console.log('starting to iterate through files');
					for(let i = 0; i < files.length; i++) {
						const file = files[i];
						if(file.data) {
							this.pages.push('');
							this.bytesToBase64DataUrl(file.data).then((val) => {
								this.pages[i] = val;
							})
							// this.test.set('data:image/png:base64,' + await this.bytesToBase64DataUrl(file.data))

						}
						console.log('finished file ' + i);
					}
					console.log('done loading');
				})
					console.log('really done loading');
					this.bookIsLoaded.set(true);
			});
		});

	}

	async bytesToBase64DataUrl(bytes: any, type = "image/webp"): Promise<string> {
		return await new Promise((resolve, reject) => {
			const reader = Object.assign(new FileReader(), {
				onload: () => resolve(reader.result as string),
				onerror: () => reject(reader.error),
			});
			reader.readAsDataURL(new File([bytes], "", { type }));
		});
	}
}
