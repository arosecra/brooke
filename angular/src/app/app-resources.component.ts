import { Component, inject, resource, ViewEncapsulation } from '@angular/core';
import { AppComponent } from './app.component';
import { CbtService } from './cbt/cbt.service';
import { LibraryDB } from './db/library-db';
import { Collection } from './model/collection';
import { Item } from './model/item';
import { Library } from './model/library';
import { Page } from './model/page';
import { Files } from './fs/library-fs';

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
	private files = inject(Files);

  storedLibrary = resource<Library, void>({
    loader: async ({ params, abortSignal }): Promise<Library> => {
      return await this.appDb.getLibrary();
    },
  });

  bookCbt = resource<
    Page[], void
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
				if(library && collection && item) {
					const cacheDirectory = library?.cacheDirectory?.handle as FileSystemDirectoryHandle;
					const cachedFilename = item?.name + '.' + collection?.itemExtension;
					const cacheFileHandle = await this.files.getFileHandle(cacheDirectory, cachedFilename);

					const itemHandle = await this.files.getFileHandle(collection.handle, 
						item.pathFromCategoryRoot + '/' + cachedFilename
					)

					if(cacheFileHandle) {
						this.cbt.loadCbtGz(item, cacheFileHandle).then((val) => resolve(val));
					} else if (itemHandle) {
						this.cbt.loadCbtGz(item, itemHandle).then((val) => resolve(val));
					} else {
						reject('could not find file');
					}
				} else {
					reject('location information not available yet');
				}
			})

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
