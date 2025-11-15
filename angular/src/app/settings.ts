import { Component, inject, signal, ViewEncapsulation } from '@angular/core';
import { AppComponent } from './app';
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
import { Thumbnail } from './model/thumbnail';

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

			<table mat-table [dataSource]="app.resources()?.storedLibrary?.value()?.collections || []">

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
						<div  class="flex">
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
			@let cacheDirectory = app.resources()?.storedLibrary?.value()?.cacheDirectory;
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
			

			<button mat-raised-button (click)="app.appState()?.showSettingsManual?.set(false)">Done</button>

		}

  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class SettingsComponent {
  app = inject(AppComponent);
  appDB = inject(LibraryDB);
  files = inject(Files);

	busy = signal<boolean>(false);
	busyMessage = signal<string>("not busy");

	async requestPermission(permissable: Collection | CacheDirectory) {
		this.busy.set(true);
		this.busyMessage.set("Requesting Permission");
		permissable.hasPermission = await this.files.getReadWritePermission(permissable.handle);
		this.app.resources()?.storedLibrary.reload();
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
    this.app.resources()?.storedLibrary.reload();
		if(!alreadyBusy) {
			this.busy.set(false);
		}
	}

	async setLocalCacheDirectory() {
    let handle: FileSystemDirectoryHandle = await window.showDirectoryPicker();
		
		this.busy.set(true);
		this.busyMessage.set("Setting cache directory");

		await this.files.getReadWritePermission(handle);
		
    await this.appDB.addSetting({
				name: 'cacheDirectory',
				value: handle
			});
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
		let itemToCategory: Record<string, Category> = {};
		category.forEach((cat) => {
			cat.items.forEach((itemRef) => {
				itemToCategory[itemRef.name] = cat;
				itemRef.childItems?.forEach((itemRef) => {
					itemToCategory[itemRef.name] = cat;
				})
			})
		})

    let fileExtensionFSEntries = Object.values(currentDirectory).filter((fsEntry) => {
      return fsEntry.name.endsWith(`${collection.itemExtension}`);
    });
    console.log(fileExtensionFSEntries);

    let {items, thumbs} = await this.findItemsFromFiles(fileExtensionFSEntries, currentDirectory, collection, itemToCategory);

    let library = new Library({
      collections: [collection],
      categories: category,
      items: items,
      settings: []
    });

    await this.appDB.addLibrary(library);
		await this.appDB.addThumbnails(thumbs);

    this.app.resources()?.storedLibrary.reload();
		
		this.busy.set(false);
	}

	private async findItemsFromFiles(
		fileExtensionFSEntries: FSEntry[], 
		currentDirectory: Record<string, FSEntry>, 
		collection: Collection, 
		itemToCategory: Record<string, Category>	
	) {
		let itemsByPath: Record<string, Item> = {};
		let thumbs: Thumbnail[] = [];
		for (let i = 0; i < fileExtensionFSEntries.length; i++) {
			let fsEntry = fileExtensionFSEntries[i];

			let seriesPath = this.files.getParentDirectoryForDirectoryPath(fsEntry.parentPath);
			let seriesFSEntry = currentDirectory[seriesPath];
			let seriesThumbnailFile = currentDirectory[`${seriesPath}/thumbnail.webp`]?.handle;

			if(seriesThumbnailFile) {
				let seriesItem = itemsByPath[seriesFSEntry.path];
				if (!seriesItem) {
					seriesItem = await this.createSeriesItem(
						currentDirectory,
						seriesFSEntry,
						collection.name
					);
					itemsByPath[seriesFSEntry.path] = seriesItem;

					thumbs.push({
						itemName: seriesItem.name,
						collectionName: collection.name,
						categoryName: itemToCategory[seriesItem.name]?.name ?? 'unassigned',
						thumbnail: await this.files.getImageFileContents(seriesThumbnailFile as FileSystemFileHandle)
					});
				}
				seriesItem.childItems?.push(await this.createItem(
					currentDirectory,
					fsEntry,
					collection.name,
					collection.itemExtension
				));

			} else {
				
				let item: Item = await this.createItem(
					currentDirectory,
					fsEntry,
					collection.name,
					collection.itemExtension
				);
				itemsByPath[item.pathFromCategoryRoot] = item;

				let thumbnailFile = currentDirectory[`${fsEntry.parentPath}/thumbnail.webp`]
					?.handle as FileSystemFileHandle;
				if (thumbnailFile) {
					thumbs.push({
						itemName: item.name,
						collectionName: collection.name,
						categoryName: itemToCategory[item.name]?.name ?? 'unassigned',
						thumbnail: await this.files.getImageFileContents(thumbnailFile)
					})
				}
			}

		}

		const items = Object.values(itemsByPath);
		return {items, thumbs};
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
			handle: fsEntry.handle as FileSystemFileHandle,
			series: false,
			childItems: []
		};
    let cbtDetailsFile = currentDirectory[`${fsEntry.parentPath}/cbtDetails.yaml`]
      ?.handle as FileSystemFileHandle;

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
			pathFromCategoryRoot: fsEntry.path,
			childItems: []
		};
    return item;
  }
}
