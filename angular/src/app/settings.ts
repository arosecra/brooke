import { Component, inject, ViewEncapsulation } from '@angular/core';
import { App } from './app';
import { LibraryDB } from './db/library-db';
import { MatButtonModule } from '@angular/material/button';
import { Files } from './fs/library-fs';
import { NewCategory, NewCollection, Item } from './app-model';
import { FSEntry } from './fs/fs-entry';
import { Library } from './model/library';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'settings',
  imports: [MatButtonModule, MatIconModule],
  template: `
    <h2>Settings</h2>
    <h3>Collections</h3>

    @for (collection of app.resources.storedLibrary.value()?.collections; track collection.name) {
      <div>
        {{ collection.name }}

        <span class="spacer"></span>
        @if (collection.hasPermission) {
          <mat-icon fontSet="material-symbols-outlined">check_circle</mat-icon>
        } @else {
          <mat-icon fontSet="material-symbols-outlined">close</mat-icon>
        }
      </div>
    }

    <button mat-raised-button (click)="addCollection()">Add Collection</button>

		<h3>Cache Directory</h3>
		@if(!app.resources.storedLibrary.value()?.settingsByName?.['cacheDirectory']) {
			<button mat-raised-button (click)="setLocalCacheDirectory()">Set Local Directory</button>
		} @else {
			<div>{{ app.resources.storedLibrary.value()?.settingsByName?.['cacheDirectory']?.name }}</div>
		}
		

    <button mat-raised-button (click)="app.appState.showSettingsManual.set(false)">Done</button>
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class Settings {
  app = inject(App);
  appDB = inject(LibraryDB);
  files = inject(Files);

	async setLocalCacheDirectory() {
    let handle: FileSystemDirectoryHandle = await window.showDirectoryPicker();
		
    let library = new Library({
      collections: [],
      categories: [],
      items: [],
      settings: [{
				name: 'cacheDirectory',
				value: handle
			}],
			cachedItems: []
    });

    await this.appDB.addLibrary(library);

	}


  async addCollection() {
    let handle: FileSystemDirectoryHandle = await window.showDirectoryPicker();

    let currentDirectory = await this.files.getFiles(handle);
    console.log(Object.entries(currentDirectory));
    console.log(handle);

    let collectionFSEntry = currentDirectory['/collection.yaml'];
    let categoriesFSEntry = currentDirectory['/categories.yaml'];

    let collection = await this.files.getYAMLFileContents<NewCollection>(
      collectionFSEntry.handle as FileSystemFileHandle,
    );
    let category = await this.files.getYAMLFileContents<NewCategory[]>(
      categoriesFSEntry.handle as FileSystemFileHandle,
    );

    collection.handle = handle;

    category.forEach((cat) => (cat.collectionName = collection.name));
    console.log(collection, category);

    let fileExtensionFSEntries = Object.values(currentDirectory).filter((fsEntry) => {
      return fsEntry.name.endsWith(`${collection.itemExtension}`);
    });
    console.log(fileExtensionFSEntries);

    let itemsByPath: Record<string, Item> = {};
    for (let i = 0; i < fileExtensionFSEntries.length; i++) {
      let fsEntry = fileExtensionFSEntries[i];
      let item: Item = await this.createItem(
        currentDirectory,
        fsEntry,
        collection.name,
        collection.itemExtension,
      );

      let seriesPath = this.files.getParentDirectoryForDirectoryPath(fsEntry.parentPath);
      let seriesFSEntry = currentDirectory[seriesPath];
      let thumbnailFile = currentDirectory[`${seriesPath}/thumbnail.png`]?.handle;
      let largeThumbnailFile = currentDirectory[`${seriesPath}/large_thumbnail.png`]?.handle;

      let isSeries = thumbnailFile || largeThumbnailFile;

      if (isSeries) {
        let seriesItem = itemsByPath[seriesFSEntry.parentPath];
        if (!seriesItem) {
          seriesItem = await this.createSeriesItem(
            currentDirectory,
            seriesFSEntry,
            collection.name,
          );
          itemsByPath[seriesFSEntry.parentPath] = seriesItem;
        }
        seriesItem.childItems?.push(item);
      } else {
        itemsByPath[item.pathFromCategoryRoot] = item;
      }
    }
    console.log(itemsByPath);

    let allItems = Object.values(itemsByPath);

    let library = new Library({
      collections: [collection],
      categories: category,
      items: allItems,
      settings: [],
			cachedItems: []
    });

    await this.appDB.addLibrary(library);

    this.app.resources.storedLibrary.reload();

    //find the collection.yaml and categories.yaml files
    // read their contents

    //find all of the files in our list that have the item extension in the collection yaml file
    // create an item for each of these
    // check if the parent directory has a thumbnail or large thumbnail. if so, create a series for it and add this item to it
    // check if there's a toc file
    // check if there's a cbtDetails file
    // read in the toc / cbtDetails / thumbnail / large_thumbnail files
    // they go with the item

    //create a list of the files in question (for sync'ing later) - hopefully with timestamps
    //  only need to keep files we used, don't need directories

    //once we have all of this, add the new entries to the db & trigger a reload of the saved library
    //
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
    };
    let thumbnailFile = currentDirectory[`${fsEntry.parentPath}/thumbnail.png`]
      ?.handle as FileSystemFileHandle;
    let largeThumbnailFile = currentDirectory[`${fsEntry.parentPath}/large_thumbnail.png`]
      ?.handle as FileSystemFileHandle;
    let cbtDetailsFile = currentDirectory[`${fsEntry.parentPath}/cbtDetails.yaml`]
      ?.handle as FileSystemFileHandle;

    if (thumbnailFile) {
      item.thumbnail = await this.files.getImageFileContents(thumbnailFile);
    }
    if (largeThumbnailFile) {
      item.largeThumbnail = await this.files.getImageFileContents(largeThumbnailFile);
    }
    if (cbtDetailsFile) {
      item.bookDetails = await this.files.getYAMLFileContents(cbtDetailsFile);
    }
    return item;
  }

  private async createSeriesItem(
    currentDirectory: Record<string, FSEntry>,
    fsEntry: FSEntry,
    collectionName: string,
  ) {
    let item: Item = {
      name: fsEntry.name,
      collectionName,
      series: true,
      pathFromCategoryRoot: fsEntry.parentPath,
      childItems: [],
    };
    let thumbnailFile = currentDirectory[`${fsEntry.parentPath}/thumbnail.png`]
      ?.handle as FileSystemFileHandle;
    let largeThumbnailFile = currentDirectory[`${fsEntry.parentPath}/large_thumbnail.png`]
      ?.handle as FileSystemFileHandle;

    if (thumbnailFile) {
      item.thumbnail = await this.files.getImageFileContents(thumbnailFile);
    }
    if (largeThumbnailFile) {
      item.largeThumbnail = await this.files.getImageFileContents(largeThumbnailFile);
    }
    return item;
  }
}
