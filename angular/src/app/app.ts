import { Location } from '@angular/common';
import {
	Component,
	HostListener, inject,
	viewChild, ViewEncapsulation
} from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { AppHeader } from './app-header';
import { AppResources } from './app-resources.service';
import { AppState } from './app-state.service';
import { AppWidgets } from './app-widgets.service';
import { CbtService } from './cbt/cbt.service';
import { LibraryDB } from './db/library-db';
import { Files } from './fs/library-fs';
import { Book } from './media-components/book';
import { CollectionBrowser } from './media-components/collection-browser';
import { LibrarySettings } from './media-components/library-settings';
import { Series } from './media-components/series';
import { CachedFile } from './model/cached-file';
import { Category } from './model/category';
import { Collection } from './model/collection';
import { Item } from './model/item';
import { ItemRef } from './model/item-ref';
import { Library } from './model/library';
import { Settings } from './settings';

@Component({
  selector: 'app',
  imports: [AppHeader, Book, CollectionBrowser, Series, Settings, LibrarySettings, AppState, AppResources, AppWidgets],
  template: `
		<app-state></app-state>
		<app-resources></app-resources>
		<app-widgets></app-widgets>
    <app-header class="row-flex row-flex-align-center"/>
    <main>
			@let wdgts = widgets();
			@if(wdgts) {
				@if (wdgts.panel.showLoading()) {
					<div>Loading</div>
				} @else if (wdgts.panel.showSettings()) {
					<settings></settings>
				} @else if (wdgts.panel.showBook()) {
					<book></book>
				} @else if (wdgts.panel.showSeries()) {
					<series></series>
				} @else if (wdgts.panel.showLibrarySettings()) {
					<library-settings></library-settings>
				} @else {
					<collection-browser></collection-browser>
				}

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
  private location = inject(Location);
  private appDb = inject(LibraryDB);
  private files = inject(Files);

  @HostListener('document:keydown', ['$event'])
  protected handleKeyboardEvent(event: KeyboardEvent) {
    if (this.widgets()?.panel.showBook()) {
      if (event.key === 'ArrowRight') {
        this.goToNextPage();
      } else if (event.key === 'ArrowLeft') {
        this.goToPreviousPage();
      }
    }
  }

  widgets = viewChild(AppWidgets);
  appState = viewChild(AppState);
  resources = viewChild(AppResources);

  handlePaginationEvent(e: PageEvent) {
    // console.log(e);
    this.goToPageSet(e.pageIndex);
  }


  constructor() {
    // const loc = this.location.path().split('/');
    // if(loc.length > 0) {
    // }
  }

  onCategoryButtonClick(category: Category) {
    if (this.appState()?.currentCategory()?.name === category.name) {
      this.appState()?.currentCategory.update(() => undefined);
    } else {
      this.appState()?.currentCategory.update(() => category);
    }
    this.appState()?.currentSeries.update(() => undefined);
    this.appState()?.currentItem.update(() => undefined);
    this.setLocation();
  }

  openHome() {
    this.appState()?.currentCollection.set(undefined);
    this.appState()?.currentCategory.set(undefined);
    this.appState()?.currentSeries.set(undefined);
    this.appState()?.currentItem.set(undefined);
    this.setLocation();
  }

  openCollection(collection?: Collection) {
		if(collection)
			this.appState()?.currentCollection.set(collection);
    this.appState()?.currentCategory.set(undefined);
    this.appState()?.currentSeries.set(undefined);
    this.appState()?.currentItem.set(undefined);
    this.setLocation();
  }

  openCategory(category?: Category) {
		if(category)
    	this.appState()?.currentCategory.set(category);
    this.appState()?.currentSeries.set(undefined);
    this.appState()?.currentItem.set(undefined);
    this.appState()?.currentPageSet.set(0);
    this.setLocation();
  }

  openItem(itemRef: ItemRef, item: Item) {
    if (this.appState()?.currentCollection()?.openType === 'video' && itemRef.series) {
      this.appState()?.currentSeries.update(() => itemRef);
      this.appState()?.currentItem.update(() => undefined);
    } else {
      let isCached = this.resources()?.storedLibrary.value()?.cachedItems.some((value) => {
        return (
          value.collectionName === this.appState()?.currentCollection()?.name &&
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
			const col =	this.appState()?.currentCollection()?.name?.toLowerCase();
    	const cat = this.appState()?.currentCategory()?.name?.toLowerCase();
    	const itm = this.appState()?.currentItem()?.name?.toLowerCase();
    	const pg = this.appState()?.currentPageSet();
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
    if (this.appState()?.currentCollection()?.openType === 'book') {
      this.displayBookItem(itemRef, item);
    } else {
      // this.displayVideoItem(item);
    }
    this.setLocation();
  }

  goToPageSet(newPageNo: number) {
    this.appState()?.currentPageSet.set(newPageNo);
    this.setLocation();

    // this.location.replaceState('test');
    window.scrollTo({ top: 0 });
  }

  goToNextPage() {
		const appState = this.appState();
		if(appState) this.goToPageSet(appState.currentPageSet() + 1);
  }

  goToPreviousPage() {
		const appState = this.appState();
    if(appState) this.goToPageSet(appState.currentPageSet() - 1);
  }

	toggleThumbnailView() {
		this.widgets()?.book.thumbnailView.update((val) => !val);
	}

  toggleOneOrTwoPageMode() {
    if (this.widgets()?.book.pagesInDisplay() === 1) {
      this.widgets()?.book.pagesInDisplay.set(2);
      this.appState()?.currentPageSet.update((val) => {
        let newVal = val / 2;
        if (val % 2) {
          newVal = (val - 1) / 2;
        }
        return newVal;
      });
    } else {
      this.appState()?.currentPageSet.update((val) => val * 2);
      this.widgets()?.book.pagesInDisplay.set(1);
    }
  }

  private displayBookItem(itemRef: ItemRef, item: Item) {
    this.appState()?.currentItem.update(() => item);

    this.appState()?.currentBookDetails.update(() => undefined);
    this.appState()?.currentPageSet.update(() => 0);
  }

  // private displayVideoItem(item: any) {
  //   this.brookeServerService
  //     .openVideo(this.appState()?.currentCollection()?.name ?? 'undefined', item.name)
  //     .subscribe(() => {
  //       this.appState()?.currentItem.update(() => item);
  //     });
  // }

  cacheItem(itemRef: ItemRef, item: Item) {
    const library = this.resources()?.storedLibrary.value() as Library;
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
      this.resources()?.storedLibrary.reload();

      this.displayItem(itemRef, item);
    });
  }
}
