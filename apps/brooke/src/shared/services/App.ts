import {
  computed,
  resource,
  Signal,
  signal,
} from '@angular/core';
import YAML from 'yaml';
import { BookDetails } from '../model/BookDetails';
import { BookViewMode } from '../model/BookViewMode';
import { Category } from '../model/Category';
import { Collection } from '../model/Collection';
import { CompleteItem } from '../model/CompleteItem';
import { Item } from '../model/Item';
import { ItemRef } from '../model/ItemRef';
import { Library } from '../model/Library';
import { location, Location } from '../model/Location';
import { MainPanelMode } from '../model/MainPanelMode';
import { Nullable } from '../model/Nullable';
import { Page } from '../model/Page';
import { PageMode } from '../model/PageMode';
import { Settings } from '../model/Settings';
import { mqSignal } from '../signals/mq-signal';
import { resourceStatusToPromise } from '../signals/res-status-to-promise';
import { AppDB } from './AppDB';
import { loadCbtGz } from './Cbt';
import { WebFS } from './WebFS';
import { ChildItem } from '../model/ChildItem';

export class App {
  busy = signal<boolean>(false);
  busyMessages = signal<string[]>([]);
  menu = signal<boolean>(false);
  fullscreen = signal<boolean>(false);
  isMobile = mqSignal('(width <= 600px)');

  location = signal<Nullable<Location>>(location());
  bookViewMode = signal<BookViewMode>('IMAGE');
  pageMode = signal<PageMode>('TWO_PAGE');
  mainPanelMode = signal<MainPanelMode>('SETTINGS');

  constructor(private appDB: AppDB) {
    this.settings.value();
    this.storedLibrary.value();
  }

  collections = resource<Collection[], void>({
    loader: async ({
      params,
      abortSignal,
    }): Promise<Collection[]> => {
      const appDB = this.appDB;
      return await appDB.getCollections();
    },
  });

  storedLibrary = resource<Library, void>({
    loader: async ({
      params,
      abortSignal,
    }): Promise<Library> => {
      const appDB = this.appDB;
      return await appDB.getLibrary();
    },
  });

  settings = resource<Settings, void>({
    loader: async ({
      params,
      abortSignal,
    }): Promise<Settings> => {
      const appDB = this.appDB;
      const settings = await appDB.getSettings();
      if (!settings.defaultPagesPer) {
        this.pageMode.set('ONE_PAGE');
      }
      return settings;
    },
  });

  settingsRequired: Signal<boolean> = computed(() => {
    const ready =
      this.storedLibrary.hasValue() &&
      this.settings.hasValue();
    if (ready) {
      const library = this.storedLibrary.value();
      const hasCollections =
        library?.collections &&
        library?.collections?.length > 0;
      const hasCollectionMissingPermissions =
        !!library?.collections.some((val: Collection) => {
          return !val.hasRWPermission;
        });

      return (
        !hasCollections || hasCollectionMissingPermissions
      );
    }
    return false;
  });

  bookCbt = resource<Page[], void>({
    loader: async ({
      params,
      abortSignal,
    }): Promise<Page[]> => {
      const settings = this.settings.value();
      const collection = this.location().collection;
      const item = this.location().item;

      return new Promise(async (resolve, reject) => {
        if (settings && collection && item) {
          const cacheDirectory =
            settings?.cacheDirectory as FileSystemDirectoryHandle;
          const cachedFilename =
            item.item.name +
            '.' +
            collection?.itemExtension;
          const cacheFileHandle = await WebFS.getFileHandle(
            cacheDirectory,
            cachedFilename,
          );
          const cacheOcrHandle = await WebFS.getFileHandle(
            cacheDirectory,
            item.item.name + '.ocr.gz',
          );
          const cacheThumbsHandle =
            await WebFS.getFileHandle(
              cacheDirectory,
              item.item.name + '.tmb.gz',
            );

          const itemHandle = item.item.handle;

          if (
            cacheFileHandle &&
            cacheOcrHandle &&
            cacheThumbsHandle
          ) {
            const cachedItem = { ...item.item };
            item.item.handle = cacheFileHandle;
            item.item.ocrHandle = cacheOcrHandle;
            item.item.thumbsHandle = cacheThumbsHandle;
            loadCbtGz(cachedItem).then((val) => {
              this.mainPanelMode.set('BOOK');
              resolve(val);
            });
          } else if (itemHandle) {
            loadCbtGz(item.item).then((val) => {
              this.mainPanelMode.set('BOOK');
              resolve(val);
            });
          } else {
            reject('could not find file');
          }
        } else {
          reject('location information not available yet');
        }
      });
    },
  });

  toggleMenu(): void {
    this.menu.update((val) => !val);
  }

  toggleAddToC() {
    throw new Error('Method not implemented.');
  }

  async updateItem() {
    const library = this.storedLibrary.value();
    const collection = this.location().collection;
    const book = this.bookCbt.value();
    const item = this.location().item;
    if (collection && book && item) {
      if (!item.item.bookDetails) {
        item.item.bookDetails = {} as BookDetails;
      }
      item.item.bookDetails.blankPages = [];
      item.item.bookDetails.imagePages = [];
      item.item.bookDetails.tocEntries = [];

      for (let i = 0; i < book.length; i++) {
        const page = book[i];
        if (page.type === 'Blank') {
          item.item.bookDetails.blankPages.push(page.name);
        } else if (page.type === 'Image') {
          item.item.bookDetails.imagePages.push(page.name);
        }

        if (page.bookmarkName) {
          item.item.bookDetails.tocEntries.push({
            name: page.bookmarkName,
            pageNumber: i,
          });
        }
      }

      const cbtDetailsHandle =
        await item.item.dirHandle.getFileHandle(
          'cbtDetails.yaml',
          { create: true },
        );
      const writableStream =
        await cbtDetailsHandle.createWritable();
      await writableStream.write(
        YAML.stringify(item.item.bookDetails),
      );
      writableStream.close();

      this.appDB.addItem(item.item as Item);
    }
  }

  onTouchStart($event: PointerEvent) {
    if (
      this.mainPanelMode() === 'BOOK' &&
      this.bookViewMode() !== 'THUMBNAIL' &&
      $event.y > 64 &&
      $event.x > 56
    ) {
      const modX = $event.x - 56;
      const modMaxX = window.innerWidth - 56;
      const percentage = (modX / modMaxX) * 100;
      if (percentage > 85) this.goToNextPage($event);
      if (percentage < 15) this.goToPreviousPage($event);
    }
  }

  openHome() {
    this.location.set(location());
    this.mainPanelMode.set('COLLECTIONS');
    this.bookCbt.reload();
    this.setLocation();
  }

  openSettings() {
    this.location.set(location());
    this.bookCbt.reload();
    this.mainPanelMode.set('SETTINGS');
    this.setLocation();
  }

  openCollection(collection: Collection) {
    return new Promise<void>(async (resolve, reject) => {
      if (collection) {
        Promise.all([
          this.appDB.getCategoriesForCollection(
            collection.name,
          ),
          this.appDB.getItemsForCollection(collection.name),
        ]).then(([categories, items]) => {
          const requiresOcrDetailsCategories =
            this.generateOcrDetailsRequiredCategory(
              collection,
              items,
            );
          const unassignedCategory =
            this.generateUnassignedCategory(
              collection,
              categories,
              items,
            );
          const alphaNums = this.generateAlphaNumCategories(
            collection,
            items,
          );

          const allCategories = [
            ...categories,
            requiresOcrDetailsCategories,
            unassignedCategory,
            ...alphaNums,
          ];

          this.location.update((location) => {
            const ret = { ...location };
            ret.collection = collection;
            ret.collectionCategories = allCategories;
            return ret;
          });

          this.bookCbt.reload();
          this.mainPanelMode.set('CATEGORIES');
          this.setLocation();
          resolve();
        });
      } else {
        reject();
      }
    });
  }

  generateAlphaNumCategories(
    collection: Collection,
    items: Item[],
  ) {
    return [...'0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ']
      .map((char) => {
        return {
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
                displayName: item.name.replaceAll('_', ' '),
              };
              return itemRef;
            }),
        };
      })
      .filter((cat: Category) => cat?.items?.length);
  }

  generateUnassignedCategory(
    collection: Collection,
    categories: Category[],
    items: Item[],
  ) {
    return {
      collectionName: collection.name,
      name: 'Unassigned',
      displayName: 'Unassigned',
      synthetic: true,
      alphabetical: false,
      items: items
        .filter((item) => {
          return !categories.find((cat) =>
            cat.items.find(
              (catItem) => catItem.name === item.name,
            ),
          );
        })
        .map((item) => {
          const itemRef: ItemRef = {
            series: false,
            childItems: [],
            name: item.name,
            displayName: item.name.replaceAll('_', ' '),
          };
          return itemRef;
        }),
    };
  }

  generateOcrDetailsRequiredCategory(
    collection: Collection,
    items: Item[],
  ) {
    return {
      collectionName: collection.name,
      name: 'Requires OCR Details',
      displayName: 'Requires OCR Details',
      synthetic: true,
      alphabetical: false,
      items: items
        .filter((item) => {
          const noBlankPages =
            !item.bookDetails?.blankPages?.length;
          const noImagePages =
            !item.bookDetails?.imagePages?.length;
          return noBlankPages && noImagePages;
        })
        .map((item) => {
          const itemRef: ItemRef = {
            series: false,
            childItems: [],
            name: item.name,
            displayName: item.name.replaceAll('_', ' '),
          };
          return itemRef;
        }),
    };
  }

  openCategory(category: Category) {
    return new Promise<boolean>((resolve) => {
      if (category) {
        const itemNames = new Set(
          category.items.map((itemRef) => itemRef.name),
        );
        const thumbs = category.synthetic
          ? this.appDB.getThumbnailsForCollectionAndItems(
              category.collectionName,
              itemNames,
            )
          : this.appDB.getThumbnailsForCollectionAndCategory(
              category.collectionName,
              category.name,
            );

        thumbs.then((thumbnails) => {
          const completeItems: CompleteItem[] = [];

          for (let i = 0; i < category.items.length; i++) {
            const itemRef = category.items[i];
            const key =
              category.collectionName + '_' + itemRef.name;
            const item =
              this.storedLibrary.value()!
                .itemsByCollectionAndName[key];
            const thumbnail = thumbnails[itemRef.name];

            completeItems.push({
              itemRef,
              item,
              thumbnail,
            });
          }

          this.location.update((location) => {
            const ret = { ...location };
            ret.series = null;
            ret.item = null;
            ret.pageSet = 0;
            ret.category = category;
            ret.completeItems = completeItems;
            return ret;
          });

          this.mainPanelMode.set('ITEMS');
          this.setLocation();
          resolve(true);
        });
      } else {
        resolve(true);
      }
    });
  }

  openItem(item: CompleteItem): Promise<any> {
    if (item.item.series) {
      this.location.update((location) => {
        const ret = { ...location };
        ret.series = item;
        return ret;
      });
      this.mainPanelMode.set('SERIES');
      return Promise.resolve();
    } else {
      let res: Promise<any> = Promise.resolve(true);
      if (this.location().collection?.openType === 'book') {
        res = this.displayBookItem(item);
      } else {
        // this.displayVideoItem(item);
      }
      this.setLocation();
      return res;
    }
  }

  openItemThumbnails(item: CompleteItem) {
    this.bookViewMode.set('THUMBNAIL');
    return this.displayBookItem(item);
  }

  openItemMarkdown(item: CompleteItem) {
    this.bookViewMode.set('MARKDOWN');
    return this.displayBookItem(item);
  }
  openCompare(item: CompleteItem) {
    this.bookViewMode.set('COMPARE');
    return this.displayBookItem(item);
  }

  toggleOneOrTwoPageMode() {
    if (this.pageMode() === 'ONE_PAGE') {
      this.pageMode.set('TWO_PAGE');
    } else {
      this.pageMode.set('ONE_PAGE');
    }
    return Promise.resolve(true);
  }

  async textToSpeech() {
    let pagesInDisplay =
      this.pageMode() === 'TWO_PAGE' ? 2 : 1;
    let i = this.location().pageSet ?? 0 * pagesInDisplay;
    const book = this.bookCbt.value();
    const voice = this.settings.value()?.voice;
    if (book && voice) {
      this.appDB.orator.readBook(
        book,
        voice,
        i,
        pagesInDisplay,
        this.goToNextPage.bind(this),
      );
    }
    return Promise.resolve(true);
  }

  stopTextToSpeech() {
    this.appDB.orator.stop();
    return Promise.resolve(true);
  }

  setLocation() {
    // private location = inject(Location);
    // const col = this.currentCollection()?.name?.toLowerCase();
    // const cat = this.currentCategory()?.name?.toLowerCase();
    // const itm = this.currentItem()?.name?.toLowerCase();
    // const pg =  this.currentPageSet();
    // if (col) {
    //   if (cat) {
    //     if (itm) {
    //       if (pg) {
    //         this.location.replaceState(`${col}/${cat}/${itm}/${pg}`);
    //       } else {
    //         this.location.replaceState(`${col}/${cat}/${itm}`);
    //       }
    //     } else {
    //       this.location.replaceState(`${col}/${cat}`);
    //     }
    //   } else {
    //     this.location.replaceState(`${col}`);
    //   }
    // } else {
    //   this.location.replaceState('');
    // }
  }

  goToPageSet(newPageNo: number) {
    const pages = this.bookCbt.value()?.length ?? 0;
    if (0 <= newPageNo && newPageNo < pages) {
      this.location.update((location) => {
        const ret = { ...location };
        ret.pageSet = newPageNo;
        return ret;
      });
      this.setLocation();

      this.scrollToTop();
    }
  }

  scrollToTop() {
    document.getElementsByTagName('main')?.[0]?.scrollTo({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });
  }

  goToNextPage(event?: Event): Promise<boolean> {
    let pagesInDisplay =
      this.pageMode() === 'TWO_PAGE' ? 2 : 1;
    this.goToPageSet(
      (this.location().pageSet ?? 0) + pagesInDisplay,
    );
    event?.preventDefault();
    return Promise.resolve(true);
  }

  goToPreviousPage(event?: Event): Promise<boolean> {
    let pagesInDisplay =
      this.pageMode() === 'TWO_PAGE' ? 2 : 1;
    this.goToPageSet(
      (this.location().pageSet ?? 0) - pagesInDisplay,
    );
    event?.preventDefault();
    return Promise.resolve(true);
  }

  openBookOptions() {}
  openTOC() {}
  openTOCPlus() {}

  toggleFullScreen(): Promise<boolean> {
    this.fullscreen.update((value) => !value);
    // if (window.document.fullscreenElement) {
    //   window.document.exitFullscreen();
    //   this.fullscreen.set(false);
    // } else {
    //   document
    //     .getElementsByName('section')?.[0]
    //     ?.requestFullscreen();
    //   // this.fullScreenTarget.nativeElement.requestFullscreen();
    //   this.fullscreen.set(true);
    // }
    return Promise.resolve(true);
  }

  private displayBookItem(item: CompleteItem) {
    this.location.update((location) => {
      const ret = { ...location };
      ret.item = item;
      return ret;
    });
    this.bookCbt.reload();
    return resourceStatusToPromise(this.bookCbt).then(
      () => {
        this.location.update((location) => {
          const ret = { ...location };
          ret.item = item;
          ret.pageSet = 0;
          return ret;
        });
      },
    );
  }

  cacheItem(item: Item | ChildItem): Promise<boolean> {
    const settings = this.settings.value();
    const promises = [];
    if (item.handle && settings?.cacheDirectory)
      promises.push(
        WebFS.copyFile(
          item.handle,
          settings.cacheDirectory,
        ),
      );
    if (item.ocrHandle && settings?.cacheDirectory)
      promises.push(
        WebFS.copyFile(
          item.ocrHandle,
          settings.cacheDirectory,
        ),
      );
    if (item.thumbsHandle && settings?.cacheDirectory)
      promises.push(
        WebFS.copyFile(
          item.thumbsHandle,
          settings.cacheDirectory,
        ),
      );
    return Promise.all(promises).then((results) =>
      results.every((result) => result === true),
    );
  }
}
