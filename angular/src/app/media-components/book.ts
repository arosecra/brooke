import { Component, effect, inject, OnInit, signal, viewChild, ViewEncapsulation } from '@angular/core';
import { App } from '../app';

import { parseTar } from "nanotar";
import { Files } from '../fs/library-fs';


@Component({
  selector: 'book',
  imports: [],
  template: `
	@if(bookIsLoaded()) {
		@let leftPageNo = app.appState.currentPageSet() * 2;
		@let rightPageNo = app.appState.currentPageSet() * 2 + 1;
		@if(pageOrder.length > leftPageNo) {
			<img #leftPage
				style="width: 100%"
				[src]="pages[pageOrder[leftPageNo]].fullPage"
			/>
		}
		@if(pageOrder.length > rightPageNo) {
			<img #rightPage
				style="width: 100%"
				[src]="pages[pageOrder[rightPageNo]].fullPage"
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

	pages: Record<string, Page> = {};

	pageOrder: string[] = []

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
							let type = 'fullPage'
							if(file.name.startsWith('.md')) {
								type = 'markdown';
							} else if(file.name.startsWith('.thumbnails')) {
								type = 'thumbnail';
							}

							let name = file.name.replaceAll('.md', '').replaceAll('.webp', '');							
							if(type !== 'fullPage') {
								name = name.split('/')[1]
							} 
							if(!this.pages[name]) {
								this.pages[name] = { name } as Page;
							}

							this.bytesToBase64DataUrl(file.data).then((val) => {
								if(type === 'fullPage') this.pages[name].fullPage = val;
								if(type === 'markdown') this.pages[name].markdown = val;
								if(type === 'thumbnail') this.pages[name].thumbnail = val;

								if(i === files.length - 1) {
									console.log('really done loading');
									this.bookIsLoaded.set(true);
								}
							})
							// this.test.set('data:image/png:base64,' + await this.bytesToBase64DataUrl(file.data))

						}
						console.log('finished file ' + i);
					}
					let keys = Object.keys(this.pages).sort();
					for(let i = 0; i < keys.length; i++ ) {
						this.pageOrder.push(keys[i]);
					}
					console.log('done loading');
				})
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

export declare interface Page {
	markdown: string;
	thumbnail: string;
	fullPage: string;
	name: string;
}