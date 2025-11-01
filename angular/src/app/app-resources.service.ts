import { Component, inject, resource, ViewEncapsulation } from '@angular/core';
import { App } from './app';
import { CbtService } from './cbt/cbt.service';
import { LibraryDB } from './db/library-db';
import { Collection } from './model/collection';
import { Item } from './model/item';
import { Library } from './model/library';
import { Page } from './model/page';

@Component({
  selector: 'app-resources',
  imports: [],
  template: ``,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
  providers: [],
})
export class AppResources {
  appDb = inject(LibraryDB);
  app = inject(App);
  cbt = inject(CbtService);

  storedLibrary = resource<Library, void>({
    loader: async ({ params, abortSignal }): Promise<Library> => {
      return await this.appDb.getLibrary();
    },
  });

  bookCbt = resource<
    Page[],
    {
      library: Library | undefined;
      collection: Collection | undefined;
      item: Item | undefined;
    }
  >({
    loader: async ({ params, abortSignal }): Promise<Page[]> => {
      const library = params.library;
      const collection = params.collection;
      const item = params.item;
			return new Promise(async (resolve, reject) => {
				if(library && collection && item) {
					const cacheDirectory = library?.cacheDirectory?.handle as FileSystemDirectoryHandle;
					const cachedFilename = item?.name + '.' + collection?.itemExtension;
					const cacheFileHandle = await cacheDirectory.getFileHandle(cachedFilename);

					if(cacheFileHandle) {
						this.cbt.loadCbt(item, cacheFileHandle).then((val) => resolve(val));
					} else {
						reject('cached file not ready');
					}
				} else {
					reject('location information not available yet');
				}
			})

    },

    params: () => ({
      library: this.storedLibrary.value(),
      collection: this.app.appState()?.currentCollection(),
      item: this.app.appState()?.currentItem(),
    }),
  });
}
