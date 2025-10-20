import { Location } from '@angular/common';
import {
	Component,
	computed,
	HostListener,
	inject,
	resource,
	signal,
	ViewEncapsulation
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BookDetails, CachedFile, CacheDirectory, Item, ItemRef, NewCategory, NewCollection } from './app-model';
import { Breadcrumb } from './breadcrumb';
import { LibraryDB } from './db/library-db';
import { Files } from './fs/library-fs';
import { Book } from './media-components/book';
import { CollectionBrowser } from './media-components/collection-browser';
import { Series } from './media-components/series';
import { Library } from './model/library';
import { Settings } from './settings';

@Component({
  selector: 'app',
  imports: [
    Book,
    Breadcrumb,
    CollectionBrowser,
    Series,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatPaginatorModule,
    Settings,
  ],
  template: `
    <mat-toolbar color="primary">
      <button icon-button (click)="appState.showSettingsManual.set(true)">
        <mat-icon fontSet="material-symbols-outlined">newsstand</mat-icon>
      </button>
      <h1>Brooke</h1>
      <breadcrumb></breadcrumb>
      <span class="spacer"></span>
      <!-- modify collection button -->
      <!-- book toc -->
      <!-- book options -->
      <!-- <book-page-turner></book-page-turner> -->
      @if (widgets.panel.showBook()) {
        <mat-paginator
          (page)="handlePaginationEvent($event)"
          [length]="appState.currentItem()?.bookDetails?.numberOfPages"
          [pageSize]="2"
          [disabled]="false"
          [showFirstLastButtons]="true"
					[pageIndex]="appState.currentPageSet()"
          [hidePageSize]="true"
          aria-label="Select page"
        >
        </mat-paginator>
      }
    </mat-toolbar>
    <main>
      @if (widgets.panel.showLoading()) {
        <div>Loading</div>
      } @else if (widgets.panel.showSettings()) {
        <settings></settings>
      } @else if (widgets.panel.showBook()) {
        <book></book>
      } @else if (widgets.panel.showSeries()) {
        <series></series>
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
        return !!this.appState.showSettingsManual() || !!this.appState.settingsRequired();
      }),

      showLoading: computed(() => {
        return this.resources.storedLibrary.isLoading();
      }),
    },
  };

  appState = {
    currentCollection: signal<NewCollection | undefined>(undefined),
    currentCategory: signal<NewCategory | undefined>(undefined),
    currentSeries: signal<ItemRef | undefined>(undefined),
    currentItem: signal<Item | undefined>(undefined),
    currentPageSet: signal<number>(0),
    currentBookDetails: signal<BookDetails | undefined>(undefined),

    showSettingsManual: signal<boolean>(false),

    settingsRequired: computed(() => {
			const library = this.resources.storedLibrary.value();

			const hasCollections = library?.collections && library?.collections?.length > 0;
			const hasCollectionMissingPermissions = library?.collections.some((val) => {
				return !val.hasPermission;
			});

      return !library?.cacheDirectory?.hasPermission || !hasCollections || hasCollectionMissingPermissions;
    }),
  };

  resources = {
    storedLibrary: resource<Library, void>({
      loader: async ({ params, abortSignal }): Promise<Library> => {
        return await this.appDb.getLibrary();
      },
    })
  };

  actions = {
    openItem: (item: Item) => {},
    onCategoryButtonClick: () => {},
  };

  onCategoryButtonClick(category: NewCategory) {
    if (this.appState.currentCategory()?.name === category.name) {
      this.appState.currentCategory.update(() => undefined);
    } else {
      this.appState.currentCategory.update(() => category);
    }
    this.appState.currentSeries.update(() => undefined);
    this.appState.currentItem.update(() => undefined);
  }

  openHome() {
    this.appState.currentCollection.set(undefined);
    this.appState.currentCategory.set(undefined);
    this.appState.currentSeries.set(undefined);
    this.appState.currentItem.set(undefined);
  }

  openCollection() {
    this.appState.currentCategory.set(undefined);
    this.appState.currentSeries.set(undefined);
    this.appState.currentItem.set(undefined);
  }

  openCategory() {
    this.appState.currentSeries.set(undefined);
    this.appState.currentItem.set(undefined);
		this.appState.currentPageSet.set(0);
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

	displayItem(itemRef: ItemRef, item: Item) {
				if (this.appState.currentCollection()?.openType === 'book') {
					this.displayBookItem(itemRef, item);
				} else {
					// this.displayVideoItem(item);
				}

	}

  goToPageSet(newPageNo: number) {
    this.appState.currentPageSet.set(newPageNo);

    this.location.replaceState('test');
		window.scrollTo({top: 0})
  }

  goToNextPage() {
    this.goToPageSet(this.appState.currentPageSet()+1);
  }

  goToPreviousPage() {
    this.goToPageSet(this.appState.currentPageSet() -1);
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
		const collection = library.collections.find((value) => value.name === item.collectionName) as NewCollection;
		this.files.cacheFile(library, collection, item).then((res) => {
			let newCachedItem: CachedFile = {
				collectionName: item.collectionName,
				itemName: item.name,
				filename: res as string
			}

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