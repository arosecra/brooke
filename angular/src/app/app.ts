import { Location } from '@angular/common';
import {
  Component, computed, HostListener, inject,
  resource, signal, ViewEncapsulation,
} from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { AppHeader } from './app-header';
import { LibraryDB } from './db/library-db';
import { Files } from './fs/library-fs';
import { Book } from './media-components/book';
import { CollectionBrowser } from './media-components/collection-browser';
import { Series } from './media-components/series';
import { BookDetails } from './model/book-details';
import { CachedFile } from './model/cached-file';
import { Category } from './model/category';
import { Collection } from './model/collection';
import { Item } from './model/item';
import { ItemRef } from './model/item-ref';
import { Library } from './model/library';
import { Settings } from './settings';
import { LibrarySettings } from './media-components/library-settings';

@Component({
  selector: 'app',
  imports: [AppHeader, Book, CollectionBrowser, Series, Settings, LibrarySettings],
  template: `
    <app-header class="row-flex row-flex-align-center"/>
    <main>
      @if (widgets.panel.showLoading()) {
        <div>Loading</div>
      } @else if (widgets.panel.showSettings()) {
        <settings></settings>
      } @else if (widgets.panel.showBook()) {
        <book></book>
      } @else if (widgets.panel.showSeries()) {
        <series></series>
      } @else if (widgets.panel.showLibrarySettings()) {
        <library-settings></library-settings>
      } @else {
        <collection-browser></collection-browser>
      }
    </main>
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
  providers: [],
})
export class App {
updateItem() {
throw new Error('Method not implemented.');
}
  location = inject(Location);
  appDb = inject(LibraryDB);
  files = inject(Files);

  @HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (this.widgets.panel.showBook()) {
      if (event.key === 'ArrowRight') {
        this.goToNextPage();
      } else if (event.key === 'ArrowLeft') {
        this.goToPreviousPage();
      }
    }
  }

  handlePaginationEvent(e: PageEvent) {
    // console.log(e);
    this.goToPageSet(e.pageIndex);
  }

  widgets = {
    panel: {
      showBook: computed(() => {
        return (
          this.appState.currentItem() && this.appState.currentCollection()?.openType === 'book'
        );
      }),

      showSeries: computed(() => {
        return !!this.appState.currentSeries();
      }),

      showSettings: computed(() => {
        return !!this.appState.showSettingsManual() || !!this.appState.showSettingsRequired();
      }),

			showLibrarySettings: computed(() => this.appState.showLibraryEditorManual()),

      showLoading: computed(() => {
        return this.resources.storedLibrary.isLoading();
      }),
    },
    book: {
      pagesInDisplay: signal<number>(2),
			thumbnailView: signal<boolean>(false),
    },
  };

  appState = {
    currentCollection: signal<Collection | undefined>(undefined),
    currentCategory: signal<Category | undefined>(undefined),
    currentSeries: signal<ItemRef | undefined>(undefined),
    currentItem: signal<Item | undefined>(undefined),
    currentPageSet: signal<number>(0),
    currentBookDetails: signal<BookDetails | undefined>(undefined),

    showSettingsManual: signal<boolean>(false),
		showLibraryEditorManual: signal<boolean>(false),

    showSettingsRequired: computed(() => {
      const library = this.resources.storedLibrary.value();

      const hasCollections = library?.collections && library?.collections?.length > 0;
      const hasCollectionMissingPermissions = !!library?.collections.some((val) => {
        return !val.hasPermission;
      });

      return (
        (!!library?.cacheDirectory && !library?.cacheDirectory?.hasPermission) ||
        !hasCollections ||
        hasCollectionMissingPermissions
      );
    }),
  };

  resources = {
    storedLibrary: resource<Library, void>({
      loader: async ({ params, abortSignal }): Promise<Library> => {
        return await this.appDb.getLibrary();
      },
    }),
  };

  constructor() {
    // const loc = this.location.path().split('/');
    // if(loc.length > 0) {
    // }
  }

  onCategoryButtonClick(category: Category) {
    if (this.appState.currentCategory()?.name === category.name) {
      this.appState.currentCategory.update(() => undefined);
    } else {
      this.appState.currentCategory.update(() => category);
    }
    this.appState.currentSeries.update(() => undefined);
    this.appState.currentItem.update(() => undefined);
    this.setLocation();
  }

  openHome() {
    this.appState.currentCollection.set(undefined);
    this.appState.currentCategory.set(undefined);
    this.appState.currentSeries.set(undefined);
    this.appState.currentItem.set(undefined);
    this.setLocation();
  }

  openCollection(collection?: Collection) {
		if(collection)
			this.appState.currentCollection.set(collection);
    this.appState.currentCategory.set(undefined);
    this.appState.currentSeries.set(undefined);
    this.appState.currentItem.set(undefined);
    this.setLocation();
  }

  openCategory(category?: Category) {
		if(category)
    	this.appState.currentCategory.set(category);
    this.appState.currentSeries.set(undefined);
    this.appState.currentItem.set(undefined);
    this.appState.currentPageSet.set(0);
    this.setLocation();
  }

  openItem(itemRef: ItemRef, item: Item) {
    if (this.appState.currentCollection()?.openType === 'video' && itemRef.series) {
      this.appState.currentSeries.update(() => itemRef);
      this.appState.currentItem.update(() => undefined);
    } else {
      let isCached = this.resources.storedLibrary.value()?.cachedItems.some((value) => {
        return (
          value.collectionName === this.appState.currentCollection()?.name &&
          value.itemName === item.name
        );
      });
      if (!isCached) {
        this.cacheItem(itemRef, item);
      } else {
        this.displayItem(itemRef, item);
      }
    }
  }

	setLocation() {
			const col =	this.appState.currentCollection()?.name?.toLowerCase();
    	const cat = this.appState.currentCategory()?.name?.toLowerCase();
    	const itm = this.appState.currentItem()?.name?.toLowerCase();
    	const pg = this.appState.currentPageSet();
    	if(col) {
    		if(cat) {
    			if(itm) {
    				if(pg) {
    					this.location.replaceState(`${col}/${cat}/${itm}/${pg}`);
    				} else {
    					this.location.replaceState(`${col}/${cat}/${itm}`);
    				}
    			} else {
    					this.location.replaceState(`${col}/${cat}`);
    			}
    		} else {
    					this.location.replaceState(`${col}`);
    		}
    	} else {
    		this.location.replaceState('');
    	}
	}

  displayItem(itemRef: ItemRef, item: Item) {
    if (this.appState.currentCollection()?.openType === 'book') {
      this.displayBookItem(itemRef, item);
    } else {
      // this.displayVideoItem(item);
    }
    this.setLocation();
  }

  goToPageSet(newPageNo: number) {
    this.appState.currentPageSet.set(newPageNo);
    this.setLocation();

    // this.location.replaceState('test');
    window.scrollTo({ top: 0 });
  }

  goToNextPage() {
    this.goToPageSet(this.appState.currentPageSet() + 1);
  }

  goToPreviousPage() {
    this.goToPageSet(this.appState.currentPageSet() - 1);
  }

	toggleThumbnailView() {
		this.widgets.book.thumbnailView.update((val) => !val);
	}

  toggleOneOrTwoPageMode() {
    if (this.widgets.book.pagesInDisplay() === 1) {
      this.widgets.book.pagesInDisplay.set(2);
      this.appState.currentPageSet.update((val) => {
        let newVal = val / 2;
        if (val % 2) {
          newVal = (val - 1) / 2;
        }
        return newVal;
      });
    } else {
      this.appState.currentPageSet.update((val) => val * 2);
      this.widgets.book.pagesInDisplay.set(1);
    }
  }

  private displayBookItem(itemRef: ItemRef, item: Item) {
    this.appState.currentItem.update(() => item);

    this.appState.currentBookDetails.update(() => undefined);
    this.appState.currentPageSet.update(() => 0);
  }

  // private displayVideoItem(item: any) {
  //   this.brookeServerService
  //     .openVideo(this.appState.currentCollection()?.name ?? 'undefined', item.name)
  //     .subscribe(() => {
  //       this.appState.currentItem.update(() => item);
  //     });
  // }

  cacheItem(itemRef: ItemRef, item: Item) {
    const library = this.resources.storedLibrary.value() as Library;
    const collection = library.collections.find(
      (value) => value.name === item.collectionName,
    ) as Collection;
    this.files.cacheFile(library, collection, item).then((res) => {
      let newCachedItem: CachedFile = {
        collectionName: item.collectionName,
        itemName: item.name,
        filename: res as string,
      };

      let libraryUpdates = new Library({
        collections: [],
        categories: [],
        items: [],
        settings: [],
        cachedItems: [newCachedItem],
      });

      this.appDb.addLibrary(libraryUpdates);
      this.resources.storedLibrary.reload();

      this.displayItem(itemRef, item);
    });
  }
}
