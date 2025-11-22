import { Location } from '@angular/common';
import { Component, ElementRef, HostListener, inject, Injector, viewChild, ViewEncapsulation } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import YAML from 'yaml';
import { AppActionsComponent } from './app-actions.component';
import { AppToolbarsComponent } from './toolbars/app-header.component';
import { AppResourcesComponent } from './app-resources.component';
import { AppStateComponent } from './app-state.component';
import { AppWidgetsComponent } from './app-widgets.component';
import { LibraryDB } from './db/library-db';
import { Files } from './fs/library-fs';
import { BookComponent } from './media/lectern/lectern.component';
import { CollectionBrowserComponent } from './media/collection-browser/collection-browser.component';
import { LibrarySettingsComponent } from './library-settings/library-settings.component';
import { BookDetails } from './model/book-details';
import { Category } from './model/category';
import { Collection } from './model/collection';
import { Item } from './model/item';
import { ItemRef } from './model/item-ref';
import { Library } from './model/library';
import { SettingsComponent } from './settings/settings';
import { resourceStatusToPromise } from './shared/res-status-to-promise';
import { Orator } from './audio/orator';
import { MatDrawer, MatSidenav, MatSidenavModule } from '@angular/material/sidenav'

@Component({
  selector: 'app',
  imports: [
    AppToolbarsComponent,
    BookComponent,
    CollectionBrowserComponent,
    SettingsComponent,
    LibrarySettingsComponent,
    AppStateComponent,
    AppResourcesComponent,
    AppWidgetsComponent,
    AppActionsComponent,
		MatSidenavModule,
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],

  providers: [],
})
export class AppComponent {

  private location = inject(Location);
  private appDb = inject(LibraryDB);
  private files = inject(Files);
	private injector = inject(Injector);
	private fullScreenTarget = inject(ElementRef);

  widgets = viewChild.required(AppWidgetsComponent);
  appState = viewChild.required(AppStateComponent);
  resources = viewChild.required(AppResourcesComponent);

	orator: Orator = inject(Orator);

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

  handlePaginationEvent(e: PageEvent) {
    // console.log(e);
    this.goToPageSet(e.pageIndex);
  }

  constructor() {
    // const loc = this.location.path().split('/');
    // if(loc.length > 0) {
    // }
  }

  async updateItem() {
    const library = this.resources()?.storedLibrary.value();
    const collection = this.appState()?.currentCollection();
    const book = this.resources()?.bookCbt.value();
    const item = this.appState()?.currentItem();
    if (collection && book && item) {
      if (!item.bookDetails) {
        item.bookDetails = {} as BookDetails;
      }
      item.bookDetails.blankPages = [];
      item.bookDetails.imagePages = [];
      item.bookDetails.tocEntries = [];

      for (let i = 0; i < book.length; i++) {
        const page = book[i];
        if (page.type === 'Blank') {
          item.bookDetails.blankPages.push(page.name);
        } else if (page.type === 'Image') {
          item.bookDetails.imagePages.push(page.name);
        }

        if (page.bookmarkName) {
          item.bookDetails.tocEntries.push({
            name: page.bookmarkName,
            pageNumber: i,
          });
        }
      }

      const itemHandle = await this.files.getDirectoryHandle(
        collection.handle,
        item.pathFromCategoryRoot,
      );
      const cbtDetailsHandle = await itemHandle.getFileHandle('cbtDetails.yaml', { create: true });
      const writableStream = await cbtDetailsHandle.createWritable();
      await writableStream.write(YAML.stringify(item.bookDetails));
      writableStream.close();

    }
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
    this.widgets()?.book.thumbnailView.set(false);
		this.resources()?.bookCbt.reload();
    this.setLocation();
  }

  openCollection(collection?: Collection): Promise<boolean> {
		return new Promise<boolean>((resolve) => {
			if (collection) this.appState()?.currentCollection.set(collection);
			this.appState().currentCategory.set(undefined);
			this.appState().currentCategoryThumbnails.set({});
			this.appState().currentSeries.set(undefined);
			this.appState().currentItem.set(undefined);
			this.widgets().book.thumbnailView.set(false);
			this.resources()?.bookCbt.reload();
			this.setLocation();
			resolve(true);
		})
  }

  openCategory(category?: Category): Promise<boolean> {
		return new Promise<boolean>((resolve) => {
			this.appState().currentSeries.set(undefined);
			this.appState().currentItem.set(undefined);
			this.appState().currentPageSet.set(0);
			this.widgets().book.thumbnailView.set(false);
			this.resources().bookCbt.reload();
			this.setLocation();
			
			if (category) {
				this.appDb.getThumbnailsForCollectionAndCategory(this.appState().currentCollection()!.name, category.name).then((thumbnails) => {
					this.appState().currentCategoryThumbnails.set(thumbnails);
					this.appState().currentCategory.set(category);
					resolve(true);
				})
			} else {
				resolve(true);
			}

		})
  }

  openItem(itemRef: ItemRef, item: Item): Promise<any> {
		let res: Promise<any> = Promise.resolve(true)
    if (this.appState()?.currentCollection()?.openType === 'video' && itemRef.series) {
      this.appState()?.currentSeries.update(() => itemRef);
      this.appState()?.currentItem.update(() => undefined);
    } else {
      res = this.displayItem(itemRef, item);

      // let isCached = this.resources()?.storedLibrary.value()?.cachedItems.some((value) => {
      // 	return (
      // 		value.collectionName === this.appState()?.currentCollection()?.name &&
      // 		value.itemName === item.name
      // 	);
      // });
      // if (!isCached) {
      // 	this.cacheItem(itemRef, item);
      // } else {
      // 	this.displayItem(itemRef, item);
      // }
    }
		return res;
  }
	
	openItemThumbnails(itemRef: ItemRef, item: Item) {
    this.widgets()?.book.thumbnailView.set(true);
		return this.displayBookItem(itemRef, item);
	}
	
	openItemMarkdown(itemRef: ItemRef, item: Item) {
    this.widgets()?.book.markdownView.set(true);
		return this.displayBookItem(itemRef, item);
	}

	async textToSpeech() {

		let pagesInDisplay = this.widgets().book.pagesInDisplay();
		let i = this.appState().currentPageSet() * this.widgets().book.pagesInDisplay();
		const book = this.resources().bookCbt.value();
		const voice = this.resources().storedLibrary.value()?.voice;
		if(book && voice) {
			this.orator.readBook(
				book,
				voice,
				i,
				pagesInDisplay,
				this.goToNextPage.bind(this)
			)
		}
		return Promise.resolve(true);
	}

	stopTextToSpeech() {
		this.orator.stop();
		return Promise.resolve(true);
	}

  setLocation() {
    const col = this.appState()?.currentCollection()?.name?.toLowerCase();
    const cat = this.appState()?.currentCategory()?.name?.toLowerCase();
    const itm = this.appState()?.currentItem()?.name?.toLowerCase();
    const pg = this.appState()?.currentPageSet();
    if (col) {
      if (cat) {
        if (itm) {
          if (pg) {
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

  displayItem(itemRef: ItemRef, item: Item): Promise<any> {
		let res: Promise<any> = Promise.resolve(true);
    if (this.appState()?.currentCollection()?.openType === 'book') {
      res = this.displayBookItem(itemRef, item);
    } else {
      // this.displayVideoItem(item);
    }
    this.setLocation();
		return res;
  }

  goToPageSet(newPageNo: number) {
    this.appState()?.currentPageSet.set(newPageNo);
    this.setLocation();

    // this.location.replaceState('test');
    window.scrollTo({ top: 0 });
  }

  goToNextPage(): Promise<boolean> {
    const appState = this.appState();
    if (appState) this.goToPageSet(appState.currentPageSet() + 1);
		return Promise.resolve(true);
  }

  goToPreviousPage(): Promise<boolean> {
    const appState = this.appState();
    if (appState) this.goToPageSet(appState.currentPageSet() - 1);
		return Promise.resolve(true);
  }

  toggleThumbnailView(): Promise<boolean> {
    this.widgets()?.book.thumbnailView.update((val) => !val);
		return Promise.resolve(true);
  }

	toggleMarkdownView(): Promise<boolean> {
    this.widgets()?.book.markdownView.update((val) => !val);
		return Promise.resolve(true);
	}

	toggleSideBySide(): Promise<boolean> {
		const sideBySide = this.widgets()?.book.sideBySide();
		const pageMode = this.widgets()?.book.pagesInDisplay();
		const pageModeToggleRequired = (!sideBySide && pageMode === 2) || (sideBySide && pageMode === 1)

    if(pageModeToggleRequired) {
			this.toggleOneOrTwoPageMode();
		} 
		this.widgets()?.book.sideBySide.set(!sideBySide);
		return Promise.resolve(true);
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
		return Promise.resolve(true);
  }

	toggleFullScreen(): Promise<boolean> {
		if(window.document.fullscreenElement) {
			window.document.exitFullscreen();
			this.widgets().fullscreen.set(false);
		} else {
			this.fullScreenTarget.nativeElement.requestFullscreen();
			this.widgets().fullscreen.set(true);
		}
		return Promise.resolve(true);
	}

  private displayBookItem(itemRef: ItemRef, item: Item) {
    this.appState().currentItem.update(() => item);

    this.appState().currentBookDetails.update(() => undefined);
    this.appState().currentPageSet.update(() => 0);
		this.resources().bookCbt.reload();
		return resourceStatusToPromise(this.resources().bookCbt, this.injector).then(() => {
			//TODO wait until now to show the item
			//     will need to change the widgets to not be computed
		})
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
    return this.files.cacheFile(library, collection, item); //.then((res) => {
      // let newCachedItem: CachedFile = {
      //   collectionName: item.collectionName,
      //   itemName: item.name,
      //   filename: res as string,
      // };

      // let libraryUpdates = new Library({
      //   collections: [],
      //   categories: [],
      //   items: [],
      //   settings: [],
      //   cachedItems: [newCachedItem],
      // });

      // this.appDb.addLibrary(libraryUpdates);
      // this.resources()?.storedLibrary.reload();

      // this.displayItem(itemRef, item);
    //});
  }
}
