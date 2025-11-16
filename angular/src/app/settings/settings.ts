import { Component, inject, Injector, signal, ViewEncapsulation } from '@angular/core';
import { AppComponent } from '../app';
import { LibraryDB } from '../db/library-db';
import { MatButtonModule } from '@angular/material/button';
import { Files } from '../fs/library-fs';
import { Item } from '../model/item';
import { Category } from '../model/category';
import { Collection } from '../model/collection';
import { FSEntry } from '../fs/fs-entry';
import { Library } from '../model/library';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { CacheDirectory } from '../model/cache-directory';
import { Thumbnail } from '../model/thumbnail';
import { resourceStatusToPromise } from '../shared/res-status-to-promise';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Orator } from '../web/orator';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'settings',
  imports: [MatButtonModule, MatIconModule, MatTableModule, MatInputModule, MatSelectModule, FormsModule],
  templateUrl: './settings.html',
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class SettingsComponent {
  sampleText: string = 'The quick brown fox jumped over the lazy dog';
  app = inject(AppComponent);
  appDB = inject(LibraryDB);
  files = inject(Files);
  injector = inject(Injector);

  orator = new Orator();

  busy = signal<boolean>(false);
  busyMessage = signal<string>('not busy');
  voice: SpeechSynthesisVoice;

  async requestPermission(permissable: Collection | CacheDirectory) {
    this.busy.set(true);
    this.busyMessage.set('Requesting Permission');
    permissable.hasPermission = await this.files.getReadWritePermission(permissable.handle);
    this.app.resources()?.storedLibrary.reload();
    this.busy.set(false);
  }

  async updateCollectionFromFiles(collection: Collection) {
    this.busy.set(true);
    this.busyMessage.set('Downloading Collection');
    let handle = collection.handle;
    this.removeCollection(collection);
    this.addCollectionFromHandle(handle);
    this.busy.set(false);
  }

  async removeCollection(collection: Collection) {
    const alreadyBusy = this.busy();
    if (!alreadyBusy) {
      this.busy.set(true);
      this.busyMessage.set('Removing Collection');
    }
    await this.appDB.removeCollection(collection);
    await this.appDB.removeCategories(collection);
    await this.appDB.removeItems(collection);
    await this.appDB.removeThumbnails(collection);
    this.app.resources()?.storedLibrary.reload();
    if (!alreadyBusy) {
      this.busy.set(false);
    }
  }

  async setLocalCacheDirectory() {
    let handle: FileSystemDirectoryHandle = await window.showDirectoryPicker();

    this.busy.set(true);
    this.busyMessage.set('Setting cache directory');

    await this.files.getReadWritePermission(handle);

    await this.appDB.addSetting({
      name: 'cacheDirectory',
      value: handle,
    });
    this.app.resources()?.storedLibrary.reload();

    resourceStatusToPromise(this.app.resources()?.storedLibrary, this.injector).then(() => {
      this.busy.set(false);
    });
  }

  async addCollectionFromHandle(handle: FileSystemDirectoryHandle) {
    this.busy.set(true);
    this.busyMessage.set('Adding Collection');

    await this.files.getReadWritePermission(handle);

    let currentDirectory = await this.files.getFiles(handle);

    let collectionFSEntry = currentDirectory['/collection.yaml'];
    let categoriesFSEntry = currentDirectory['/categories.yaml'];

    let collection = await this.files.getYAMLFileContents<Collection>(collectionFSEntry.handle as FileSystemFileHandle);
    let category = await this.files.getYAMLFileContents<Category[]>(categoriesFSEntry.handle as FileSystemFileHandle);

    collection.handle = handle;

    this.busyMessage.set('Collection and categories loaded');

    category.forEach((cat) => (cat.collectionName = collection.name));
    let itemToCategory: Record<string, Category> = {};
    category.forEach((cat) => {
      cat.items.forEach((itemRef) => {
        itemToCategory[itemRef.name] = cat;
        itemRef.childItems?.forEach((itemRef) => {
          itemToCategory[itemRef.name] = cat;
        });
      });
    });

    this.busyMessage.set('Categories marked for collection');

    let fileExtensionFSEntries = Object.values(currentDirectory).filter((fsEntry) => {
      return fsEntry.name.endsWith(`${collection.itemExtension}`);
    });
    this.busyMessage.set(`${fileExtensionFSEntries.length} item directories found. Processing.`);

    let { items, thumbs } = await this.findItemsFromFiles(
      fileExtensionFSEntries,
      currentDirectory,
      collection,
      itemToCategory,
    );

    let library = new Library({
      collections: [collection],
      categories: category,
      items: items,
      settings: [],
    });

    await this.appDB.addLibrary(library);
    await this.appDB.addThumbnails(thumbs);

    this.app.resources()?.storedLibrary.reload();

    resourceStatusToPromise(this.app.resources()?.storedLibrary, this.injector).then(() => {
      this.busy.set(false);
    });
  }

  private async findItemsFromFiles(
    fileExtensionFSEntries: FSEntry[],
    currentDirectory: Record<string, FSEntry>,
    collection: Collection,
    itemToCategory: Record<string, Category>,
  ) {
    let itemsByPath: Record<string, Item> = {};
    let thumbs: Thumbnail[] = [];
    for (let i = 0; i < fileExtensionFSEntries.length; i++) {
      let fsEntry = fileExtensionFSEntries[i];
      this.busyMessage.set(`Processing. ${fileExtensionFSEntries.length - i} files remaining.`);

      let seriesPath = this.files.getParentDirectoryForDirectoryPath(fsEntry.parentPath);
      let seriesFSEntry = currentDirectory[seriesPath];
      let seriesThumbnailFile = currentDirectory[`${seriesPath}/thumbnail.webp`]?.handle;

      if (seriesThumbnailFile) {
        let seriesItem = itemsByPath[seriesFSEntry.path];
        if (!seriesItem) {
          seriesItem = await this.createSeriesItem(currentDirectory, seriesFSEntry, collection.name);
          itemsByPath[seriesFSEntry.path] = seriesItem;

          thumbs.push({
            itemName: seriesItem.name,
            collectionName: collection.name,
            categoryName: itemToCategory[seriesItem.name]?.name ?? 'unassigned',
            thumbnail: await this.files.getImageFileContents(seriesThumbnailFile as FileSystemFileHandle),
          });
        }
        seriesItem.childItems?.push(
          await this.createItem(currentDirectory, fsEntry, collection.name, collection.itemExtension),
        );
      } else {
        let item: Item = await this.createItem(currentDirectory, fsEntry, collection.name, collection.itemExtension);
        itemsByPath[item.pathFromCategoryRoot] = item;

        let thumbnailFile = currentDirectory[`${fsEntry.parentPath}/thumbnail.webp`]?.handle as FileSystemFileHandle;
        if (thumbnailFile) {
          thumbs.push({
            itemName: item.name,
            collectionName: collection.name,
            categoryName: itemToCategory[item.name]?.name ?? 'unassigned',
            thumbnail: await this.files.getImageFileContents(thumbnailFile),
          });
        }
      }
    }

    const items = Object.values(itemsByPath);
    return { items, thumbs };
  }

  async addNewCollection() {
    let handle: FileSystemDirectoryHandle = await window.showDirectoryPicker();
    this.addCollectionFromHandle(handle);
  }

  async playSampleText() {
    this.busy.set(true);
    this.busyMessage.set('');
    this.orator.read(this.sampleText, this.voice.name).then(() => {
      this.busy.set(false);
    });
  }

  saveVoice() {
    this.busy.set(true);
    this.busyMessage.set('');
    this.appDB
      .addSetting({
        name: 'voice',
        value: this.voice.name,
      })
      .then(() => {
        this.app.resources()?.storedLibrary.reload();

        resourceStatusToPromise(this.app.resources()?.storedLibrary, this.injector).then(() => {
          this.busy.set(false);
        });
      });
  }

  private async createItem(
    currentDirectory: Record<string, FSEntry>,
    fsEntry: FSEntry,
    collectionName: string,
    fileExtension: string,
  ) {
    let item: Item = {
      name: fsEntry.name.replace('.' + fileExtension, ''),
      collectionName,
      pathFromCategoryRoot: fsEntry.parentPath,
      handle: fsEntry.handle as FileSystemFileHandle,
      series: false,
      childItems: [],
    };
    let cbtDetailsFile = currentDirectory[`${fsEntry.parentPath}/cbtDetails.yaml`]?.handle as FileSystemFileHandle;

    if (cbtDetailsFile) {
      item.bookDetails = await this.files.getYAMLFileContents(cbtDetailsFile);
    }
    return item;
  }

  private async createSeriesItem(currentDirectory: Record<string, FSEntry>, fsEntry: FSEntry, collectionName: string) {
    let item: Item = {
      name: fsEntry.name,
      collectionName,
      series: true,
      pathFromCategoryRoot: fsEntry.path,
      childItems: [],
    };
    return item;
  }
}
