import { inject, Injectable } from '@angular/core';
import { Library } from '../model/library';
import { Item } from '../model/item';
import { Setting } from '../model/setting';
import { Category } from '../model/category';
import { Collection } from '../model/collection';
import { openDB } from './pidb';
import { Files } from '../fs/library-fs';
import { Thumbnail } from '../model/thumbnail';
import { Orator } from '../shared/orator';

export function onUpgradeNeeded(this: IDBOpenDBRequest, event: IDBVersionChangeEvent) {
  let db = this.result;
  switch (event.oldVersion) {
    case 0:
      db.createObjectStore('collections', { keyPath: 'name' });
      db.createObjectStore('categories', { keyPath: ['collectionName', 'name'] });
      db.createObjectStore('items', { keyPath: ['collectionName', 'name'] });
      db.createObjectStore('settings', { keyPath: 'name' });
      db.createObjectStore('thumbnails', { keyPath: ['collectionName', 'categoryName', 'itemName'] });
      break;
    case 1:
			break;
  }
}

export const TABLE_NAMES = ['collections', 'categories', 'items', 'settings', 'thumbnails'];

@Injectable({
  providedIn: 'root',
})
export class LibraryDB {
  files = inject(Files);

  constructor() {}

  async getLibrary() {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readonly');

    let res = new Library({
      collections: await this.getAll<Collection[]>(tx, 'collections'),
      categories: await this.getAll<Category[]>(tx, 'categories'),
      items: await this.getAll<Item[]>(tx, 'items'),
      settings: await this.getAll<Setting[]>(tx, 'settings')
    });
    for (let i = 0; i < res.collections.length; i++) {
      res.collections[i].hasPermission = await this.files.hasReadWritePermission(
        res.collections[i].handle,
      );
    }
		const cacheSetting = res.settings?.find((val) => val.name === 'cacheDirectory');
		if(cacheSetting) {
			res.cacheDirectory = {
				handle: cacheSetting?.value,
				hasPermission:  cacheSetting && await this.files.hasReadWritePermission(cacheSetting.value)
			}
		}
		const voiceSetting = res.settings?.find((val) => val.name === 'voice');
		res.voice = voiceSetting?.value ?? new Orator().getVoices().find((voice: SpeechSynthesisVoice) => voice.default)?.name;
		
    return res;
  }

  async addLibrary(library: Library) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Collection>(tx, library.collections, 'collections');
    this.addAll<Category>(tx, library.categories, 'categories');
    this.addAll<Item>(tx, library.items, 'items');
    this.addAll<Setting>(tx, library.settings, 'settings');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async addCollection(collection: Collection) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Collection>(tx, [collection], 'collections');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async addCategory(category: Category[]) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Category>(tx, category, 'categories');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async addItems(items: Item[]) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Item>(tx, items, 'items');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

	async addThumbnails(thumbnails: Thumbnail[]) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
    this.addAll<Thumbnail>(tx, thumbnails, 'thumbnails');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
	}

  async addSetting(setting: Setting) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Setting>(tx, [setting], 'settings');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

	async removeCollection(collection: Collection) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
		await this.remove(tx, 'collections', collection.name);
	}

	async removeCategories(collection: Collection) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
		const lowerBoundKey = [collection.name];
    const upperBoundKey = [collection.name, []];
    const keyRange = IDBKeyRange.bound(lowerBoundKey, upperBoundKey);
		await this.remove(tx, 'categories', keyRange);
	}

	async removeItems(collection: Collection) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
		const lowerBoundKey = [collection.name];
    const upperBoundKey = [collection.name, []];
    const keyRange = IDBKeyRange.bound(lowerBoundKey, upperBoundKey);
		await this.remove(tx, 'items', keyRange);
	}

	async removeThumbnails(collection: Collection) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
		const lowerBoundKey = [collection.name];
    const upperBoundKey = [collection.name, [], []]; 
    const keyRange = IDBKeyRange.bound(lowerBoundKey, upperBoundKey);
		await this.remove(tx, 'thumbnails', keyRange);
	}

	async getThumbnailsForCollectionAndCategory(collectionName: string, categoryName: string) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
		const lowerBoundKey = [collectionName, categoryName];
    const upperBoundKey = [collectionName, categoryName, []]; 
    const keyRange = IDBKeyRange.bound(lowerBoundKey, upperBoundKey);
		return new Promise<Record<string, Thumbnail>>((resolve) => {
      const request = tx.objectStore('thumbnails').getAll(keyRange);
      request.onsuccess = (e) => {
				const r: Record<string, Thumbnail> = {};
				request.result.forEach((thumbnail) => r[thumbnail.itemName] = thumbnail)
				resolve(r);
			}
    });
	}

  remove(tx: IDBTransaction, objectStoreName: string, key: IDBKeyRange | IDBValidKey): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
      const request = tx.objectStore(objectStoreName).delete(key);
      request.onsuccess = (e) => resolve(true);
    });
  }

  getAll<T>(tx: IDBTransaction, objectStoreName: string) {
    return new Promise<T>((resolve) => {
      const request = tx.objectStore(objectStoreName).getAll();
      request.onsuccess = (e) => resolve(request.result as T);
    });
  }

  addAll<T>(tx: IDBTransaction, values: T[], objectStoreName: string) {
    let req = tx.objectStore(objectStoreName);
    for (let i = 0; i < values.length; i++) {
      req.put(values[i]);
    }
  }
}
