import { Location } from '@angular/common';
import { Component, ElementRef, inject, Injector, viewChild } from '@angular/core';
import YAML from 'yaml';
import { AppActionsComponent } from './app-actions.component';
import { AppToolbarsComponent } from './toolbars/app-toolbars.component';
import { AppResourcesComponent } from './app-resources.component';
import { AppStateComponent } from './app-state.component';
import { AppWidgetsComponent } from './app-widgets.component';
import { LibraryDB } from './db/library-db';
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
import { resourceStatusToPromise } from './shared/signals/res-status-to-promise';
import { Orator } from './audio/orator';
import { GalleryComponent } from './media/gallery/gallery.component';
import { WebFS } from './shared/web-fs';
import { ChildItem } from './model/child-item';

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
    GalleryComponent,
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],

  providers: [],
  host: {
    '(document:keydown.arrowRight)': 'goToNextPage($event)',
    '(document:keydown.arrowLeft)': 'goToPreviousPage($event)',
    '(document:pointerdown)': 'onTouchStart($event)',
  },
})
export class AppComponent {
  private location = inject(Location);
  private appDb = inject(LibraryDB);
  private injector = inject(Injector);
  private fullScreenTarget = inject(ElementRef);

  widgets = viewChild.required(AppWidgetsComponent);
  appState = viewChild.required(AppStateComponent);
  resources = viewChild.required(AppResourcesComponent);

  orator: Orator = inject(Orator);

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

      const cbtDetailsHandle = await item.dirHandle.getFileHandle('cbtDetails.yaml', { create: true });
      const writableStream = await cbtDetailsHandle.createWritable();
      await writableStream.write(YAML.stringify(item.bookDetails));
      writableStream.close();

      this.appDb.addItem(item as Item);
    }
  }

  onTouchStart($event: PointerEvent) {
    if (this.widgets()?.panel.showBook() && $event.y > 64 && $event.x > 56 && !this.widgets().sideNavOpen()) {
      const modX = $event.x - 56;
      const modMaxX = window.innerWidth - 56;
      const percentage = (modX / modMaxX) * 100;
      if (percentage > 85) this.goToNextPage($event);
      if (percentage < 15) this.goToPreviousPage($event);
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
    return new Promise<boolean>(async (resolve) => {
      if (collection) {
        this.appState()?.currentCollection.set(collection);
        const categories = await this.appDb.getCategoriesForCollection(collection.name);
				const items = await this.appDb.getItemsForCollection(collection.name);
				//synthesize some categories 
				const requiresOcrDetailsCategories: Category = {
					collectionName: collection.name,
					name: 'Requires OCR Details',
					displayName: 'Requires OCR Details',
					synthetic: true,
					alphabetical: false,
					items: items.filter((item) => {
						const noBlankPages = !item.bookDetails?.blankPages?.length; 
						const noImagePages = !item.bookDetails?.imagePages?.length;
						return noBlankPages && noImagePages;
					}).map((item) => { 
						const itemRef: ItemRef = {
							series: false,
							childItems: [],
							name: item.name,
							displayName: item.name.replaceAll('_', ' ')
						}
						return itemRef;

					})
				}
				categories.push(requiresOcrDetailsCategories);
				
				const alphaNum = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
				for(let i = 0; i < alphaNum.length; i++) {
					let char = alphaNum.charAt(i);
					const aphaNumCategory: Category = {
						collectionName: collection.name,
						name: char,
						displayName: char,
						synthetic: true,
						alphabetical: true,
						items: items
						.filter((item) => item.name.startsWith(char))
						.map((item) => { 
							const itemRef: ItemRef = {
								series: false,
								childItems: [],
								name: item.name,
								displayName: item.name.replaceAll('_', ' ')
							}
							return itemRef;
						})
					}
					if(aphaNumCategory.items.length) categories.push(aphaNumCategory);
				}

        this.appState().currentCollectionCategories.set(categories);
      }
      this.appState().currentCategory.set(undefined);
      this.appState().currentCategoryThumbnails.set({});
      this.appState().currentSeries.set(undefined);
      this.appState().currentItem.set(undefined);
      this.widgets().book.thumbnailView.set(false);
      this.resources()?.bookCbt.reload();
      this.setLocation();
      resolve(true);
    });
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
				const itemNames = new Set(category.items.map((itemRef) => itemRef.name));
				const thumbs = category.synthetic ? 
					this.appDb.getThumbnailsForCollectionAndItems(this.appState().currentCollection()!.name, itemNames)
					: this.appDb.getThumbnailsForCollectionAndCategory(this.appState().currentCollection()!.name, category.name)

				thumbs.then((thumbnails) => {
					this.appState().currentCategoryThumbnails.set(thumbnails);
					this.appState().currentCategory.set(category);
					resolve(true);
				});
				
      } else {
        resolve(true);
      }
    });
  }

  openSeriesItem(seriesItemRef: ItemRef, seriesItem: Item) {
    this.appState()?.currentSeries.update(() => seriesItemRef);
    this.appState()?.currentItem.update(() => undefined);
    return Promise.resolve(true);
  }

  openItem(item: Item | ChildItem): Promise<any> {
    return this.displayItem(item);
  }

  openItemThumbnails(item: Item | ChildItem) {
    this.widgets()?.book.thumbnailView.set(true);
    return this.displayBookItem(item);
  }

  openItemMarkdown(item: Item | ChildItem) {
    this.widgets()?.book.markdownView.set(true);
    return this.displayBookItem(item);
  }

  async textToSpeech() {
    let pagesInDisplay = this.widgets().book.pagesInDisplay();
    let i = this.appState().currentPageSet() * this.widgets().book.pagesInDisplay();
    const book = this.resources().bookCbt.value();
    const voice = this.resources().storedLibrary.value()?.voice;
    if (book && voice) {
      this.orator.readBook(book, voice, i, pagesInDisplay, this.goToNextPage.bind(this));
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

  displayItem(item: Item | ChildItem): Promise<any> {
    let res: Promise<any> = Promise.resolve(true);
    if (this.appState()?.currentCollection()?.openType === 'book') {
      res = this.displayBookItem(item);
    } else {
      // this.displayVideoItem(item);
    }
    this.setLocation();
    return res;
  }

  goToPageSet(newPageNo: number) {
    if (this.widgets()?.panel.showBook()) {
      const pages = this.resources().bookCbt.value()?.length ?? 0;
      if (0 <= newPageNo && newPageNo < pages) {
        this.appState().currentPageSet.set(newPageNo);
        this.setLocation();

        // this.location.replaceState('test');
        const elements = document.getElementsByTagName('main');
        if (elements?.length) elements[0].scrollIntoView();
      }
    }
  }

  goToNextPage(event?: Event): Promise<boolean> {
    this.goToPageSet(this.appState().currentPageSet() + this.widgets().book.pagesInDisplay());
    event?.preventDefault();
    return Promise.resolve(true);
  }

  goToPreviousPage(event?: Event): Promise<boolean> {
    this.goToPageSet(this.appState().currentPageSet() - this.widgets().book.pagesInDisplay());
    event?.preventDefault();
    return Promise.resolve(true);
  }

  toggleBookOptions(): Promise<boolean> {
    this.widgets().sideNav.showOptions.update((val) => !val);
    this.widgets().sideNav.showToc.set(false);
    this.widgets().sideNav.showTocAdd.set(false);
    return Promise.resolve(true);
  }

  toggleToC(): Promise<boolean> {
    this.widgets().sideNav.showToc.update((val) => !val);
    this.widgets().sideNav.showOptions.set(false);
    this.widgets().sideNav.showTocAdd.set(false);
    return Promise.resolve(true);
  }

  toggleAddToC(): Promise<boolean> {
    this.widgets().sideNav.showTocAdd.update((val) => !val);
    this.widgets().sideNav.showToc.set(false);
    this.widgets().sideNav.showOptions.set(false);
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
    const pageModeToggleRequired = (!sideBySide && pageMode === 2) || (sideBySide && pageMode === 1);

    if (pageModeToggleRequired) {
      this.toggleOneOrTwoPageMode();
    }
    this.widgets()?.book.sideBySide.set(!sideBySide);
    return Promise.resolve(true);
  }

  toggleOneOrTwoPageMode() {
    if (this.widgets()?.book.pagesInDisplay() === 1) {
      this.widgets()?.book.pagesInDisplay.set(2);
    } else {
      this.widgets()?.book.pagesInDisplay.set(1);
    }
    return Promise.resolve(true);
  }

  toggleFullScreen(): Promise<boolean> {
    if (window.document.fullscreenElement) {
      window.document.exitFullscreen();
      this.widgets().fullscreen.set(false);
    } else {
      this.fullScreenTarget.nativeElement.requestFullscreen();
      this.widgets().fullscreen.set(true);
    }
    return Promise.resolve(true);
  }

  private displayBookItem(item: Item | ChildItem) {
    this.appState().currentItem.update(() => item);

    this.appState().currentBookDetails.update(() => undefined);
    this.appState().currentPageSet.update(() => 0);
    this.resources().bookCbt.reload();
    return resourceStatusToPromise(this.resources().bookCbt, this.injector).then(() => {
      //TODO wait until now to show the item
      //     will need to change the widgets to not be computed
    });
  }

  // private displayVideoItem(item: any) {
  //   this.brookeServerService
  //     .openVideo(this.appState()?.currentCollection()?.name ?? 'undefined', item.name)
  //     .subscribe(() => {
  //       this.appState()?.currentItem.update(() => item);
  //     });
  // }

  cacheItem(item: Item | ChildItem): Promise<boolean> {
    const library = this.resources()?.storedLibrary.value() as Library;
    if (item.handle && library.cacheDirectory) return WebFS.copyFile(item.handle, library.cacheDirectory);
    return Promise.resolve(false);
  }
}
