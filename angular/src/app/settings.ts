import { Component, inject, signal, ViewEncapsulation } from '@angular/core';
import { App } from './app';
import { LibraryDB } from './db/library-db';
import { MatButtonModule } from '@angular/material/button';
import { Files } from './fs/library-fs';
import { Item } from './model/item';
import { Category } from './model/category';
import { Collection } from './model/collection';
import { FSEntry } from './fs/fs-entry';
import { Library } from './model/library';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { CacheDirectory } from './model/cache-directory';

@Component({
  selector: 'settings',
  imports: [MatButtonModule, MatIconModule, MatTableModule],
  template: `
    <h2>Settings</h2>
		@if(busy()) {
			<div>Busy. Please wait...</div>
			<div>{{ busyMessage() }}</div>
		} @else {
    	<h3>Collections</h3>

			<table mat-table [dataSource]="app.resources.storedLibrary.value()?.collections || []">

				<ng-container matColumnDef="name">
					<th mat-header-cell *matHeaderCellDef> Name </th>
    			<td mat-cell *matCellDef="let row"> {{row.name}} </td>
				</ng-container>

				<ng-container matColumnDef="permission">
					<th mat-header-cell *matHeaderCellDef> Permission </th>
    			<td mat-cell *matCellDef="let row"> 
						@if(!row.hasPermission) {
						<button matMiniFab (click)="requestPermission(row)" title="Request Permission">
							<mat-icon fontSet="material-symbols-outlined">close</mat-icon>
						</button>
						}	@else {
							<mat-icon fontSet="material-symbols-outlined">check_circle</mat-icon>
						}
					</td>
				</ng-container>

				<ng-container matColumnDef="actions">
					<th mat-header-cell *matHeaderCellDef> Actions </th>
    			<td mat-cell *matCellDef="let row">  
						<div  class="row-flex">
						<button matMiniFab (click)="removeCollection(row)" title="Remove">
							<mat-icon fontSet="material-symbols-outlined">close</mat-icon>
						</button> 
						<button matMiniFab (click)="updateCollectionFromFiles(row)" title="Re-Download">
							<mat-icon fontSet="material-symbols-outlined">sync_arrow_down</mat-icon>
						</button>
						</div>
					</td>
				</ng-container>

				<tr mat-header-row *matHeaderRowDef="['name', 'permission', 'actions']"></tr>
  			<tr mat-row *matRowDef="let row; columns: ['name', 'permission', 'actions']"></tr>
			</table>

			<button mat-raised-button (click)="addNewCollection()">Add Collection</button>

			<h3>Cache Directory</h3>
			@let cacheDirectory = app.resources.storedLibrary.value()?.cacheDirectory;
			@if(!cacheDirectory) {
				<button mat-raised-button (click)="setLocalCacheDirectory()">Set Local Directory</button>
			} @else {

				<table mat-table [dataSource]="[cacheDirectory]">

					<ng-container matColumnDef="name">
						<th mat-header-cell *matHeaderCellDef> Name </th>
						<td mat-cell *matCellDef="let row"> Cache Directory </td>
					</ng-container>

					<ng-container matColumnDef="permission">
						<th mat-header-cell *matHeaderCellDef> Permission </th>
						<td mat-cell *matCellDef="let row"> 
							@if(!row.hasPermission) {
							<button matMiniFab (click)="requestPermission(row)" title="Request Permission">
								<mat-icon fontSet="material-symbols-outlined">close</mat-icon>
							</button>
							}	@else {
								<mat-icon fontSet="material-symbols-outlined">check_circle</mat-icon>
							}
						</td>
					</ng-container>

					<tr mat-header-row *matHeaderRowDef="['name', 'permission']"></tr>
					<tr mat-row *matRowDef="let row; columns: ['name', 'permission']"></tr>
				</table>

			}
			

			<button mat-raised-button (click)="app.appState.showSettingsManual.set(false)">Done</button>

		}

  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class Settings {
  app = inject(App);
  appDB = inject(LibraryDB);
  files = inject(Files);

	busy = signal<boolean>(false);
	busyMessage = signal<string>("not busy");

	async requestPermission(permissable: Collection | CacheDirectory) {
		this.busy.set(true);
		this.busyMessage.set("Requesting Permission");
		permissable.hasPermission = await this.files.getReadWritePermission(permissable.handle);
		this.busy.set(false);
	}

	async updateCollectionFromFiles(collection: Collection) {
		this.busy.set(true);
		this.busyMessage.set("Downloading Collection");
		let handle = collection.handle;
		this.removeCollection(collection);
		this.addCollectionFromHandle(handle);
		this.busy.set(false);
	}

	async removeCollection(collection: Collection) {
		const alreadyBusy = this.busy();
		if(!alreadyBusy) {
			this.busy.set(true);
			this.busyMessage.set("Removing Collection");
		}
		await this.appDB.removeCollection(collection);
    this.app.resources.storedLibrary.reload();
		if(!alreadyBusy) {
			this.busy.set(false);
		}
	}

	async setLocalCacheDirectory() {
    let handle: FileSystemDirectoryHandle = await window.showDirectoryPicker();
		
		this.busy.set(true);
		this.busyMessage.set("Setting cache directory");

		await this.files.getReadWritePermission(handle);

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
		this.busy.set(false);

	}

	async addCollectionFromHandle(handle: FileSystemDirectoryHandle) {
		this.busy.set(true);
		this.busyMessage.set("Adding Collection");

		await this.files.getReadWritePermission(handle);

    let currentDirectory = await this.files.getFiles(handle);
    console.log(Object.entries(currentDirectory));
    console.log(handle);

    let collectionFSEntry = currentDirectory['/collection.yaml'];
    let categoriesFSEntry = currentDirectory['/categories.yaml'];

    let collection = await this.files.getYAMLFileContents<Collection>(
      collectionFSEntry.handle as FileSystemFileHandle,
    );
    let category = await this.files.getYAMLFileContents<Category[]>(
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
		
		this.busy.set(false);
	}

  async addNewCollection() {
    let handle: FileSystemDirectoryHandle = await window.showDirectoryPicker();
		this.addCollectionFromHandle(handle);
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
