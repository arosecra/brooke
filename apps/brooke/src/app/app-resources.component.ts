import { Component, inject, resource } from '@angular/core';
import { AppComponent } from './app.component';
import { CbtService } from './cbt/cbt.service';
import { LibraryDB } from './db/library-db';
import { Library } from './model/library';
import { Page } from './model/page';
import { WebFS } from './shared/web-fs';
import { Collection } from './model/collection';

@Component({
  selector: 'app-resources',
  imports: [],
  template: ``,
  styles: ``,

  providers: [],
})
export class AppResourcesComponent {
  private appDb = inject(LibraryDB);
  private app = inject(AppComponent);
  private cbt = inject(CbtService);

  collections = resource<Collection[], void>({
    loader: async ({ params, abortSignal }): Promise<Collection[]> => {
      return await this.appDb.getCollections();
    },
  });

  storedLibrary = resource<Library, void>({
    loader: async ({ params, abortSignal }): Promise<Library> => {
      return await this.appDb.getLibrary();
    },
  });

  bookCbt = resource<
    Page[],
    void
    // {
    //   library: Library | undefined;
    //   collection: Collection | undefined;
    //   item: Item | undefined;
    // }
  >({
    loader: async ({ params, abortSignal }): Promise<Page[]> => {
      const library = this.storedLibrary.value();
      const collection = this.app.appState()?.currentCollection();
      const item = this.app.appState()?.currentItem();

      return new Promise(async (resolve, reject) => {
        if (library && collection && item) {
          const cacheDirectory = library?.cacheDirectory as FileSystemDirectoryHandle;
          const cachedFilename = item?.name + '.' + collection?.itemExtension;
          const cacheFileHandle = await WebFS.getFileHandle(cacheDirectory, cachedFilename);
          const cacheOcrHandle = await WebFS.getFileHandle(cacheDirectory, item?.name + '.ocr.gz');
          const cacheThumbsHandle = await WebFS.getFileHandle(cacheDirectory, item?.name + '.tmb.gz');

          const itemHandle = item.handle;

          if (cacheFileHandle && cacheOcrHandle && cacheThumbsHandle) {
            const cachedItem = { ...item };
            item.handle = cacheFileHandle;
            item.ocrHandle = cacheOcrHandle;
            item.thumbsHandle = cacheThumbsHandle;
            this.cbt.loadCbtGz(cachedItem).then((val) => resolve(val));
          } else if (itemHandle) {
            this.cbt.loadCbtGz(item).then((val) => resolve(val));
          } else {
            reject('could not find file');
          }
        } else {
          reject('location information not available yet');
        }
      });
    },
    // params: () => {
    //   const library = this.storedLibrary.value();
    //   const collection = this.app.appState()?.currentCollection();
    //   const item = this.app.appState()?.currentItem();

    // 	let res = undefined;
    // 	if(library && collection && item) {
    // 		res = {
    // 			library: this.storedLibrary.value(),
    // 			collection: this.app.appState()?.currentCollection(),
    // 			item: this.app.appState()?.currentItem(),
    // 		}
    // 	}
    // 	return res;

    // },
  });
}
