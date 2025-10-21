import { inject, Injectable } from '@angular/core';
import { Library } from '../model/library';
import { CachedFile } from '../model/cached-file';
import { Item } from '../model/item';
import { Setting } from '../model/setting';
import { Category } from '../model/category';
import { Collection } from '../model/collection';
import { openDB } from './pidb';
import { Files } from '../fs/library-fs';

export function onUpgradeNeeded(this: IDBOpenDBRequest, event: IDBVersionChangeEvent) {
  let db = this.result;
  switch (event.oldVersion) {
    case 0:
      db.createObjectStore('collections', { keyPath: 'name' });
      db.createObjectStore('categories', { keyPath: ['collectionName', 'name'] });
      db.createObjectStore('items', { keyPath: ['collectionName', 'name'] });
      db.createObjectStore('settings', { keyPath: 'name' });
      db.createObjectStore('cache', { keyPath: ['collectionName', 'itemName'] });
      break;
    // version 0 means that the client had no database
    // perform initialization
    case 1:
    // client had version 1
    // update
  }
}

@Injectable({
  providedIn: 'root',
})
export class LibraryDB {
  files = inject(Files);

  constructor() {}

  async getLibrary() {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(['collections', 'categories', 'items', 'settings', 'cache'], 'readonly');

    let res = new Library({
      collections: await this.getAll<Collection[]>(tx, 'collections'),
      categories: await this.getAll<Category[]>(tx, 'categories'),
      items: await this.getAll<Item[]>(tx, 'items'),
      settings: await this.getAll<Setting[]>(tx, 'settings'),
			cachedItems: await this.getAll<CachedFile[]>(tx, 'cache'),
    });
    for (let i = 0; i < res.collections.length; i++) {
      res.collections[i].hasPermission = await this.files.hasReadWritePermission(
        res.collections[i].handle,
      );
    }
		const setting = res.settings?.find((val) => val.name === 'cacheDirectory');
		res.cacheDirectory = {
			handle: setting?.value,
			hasPermission:  setting && await this.files.hasReadWritePermission(setting.value)
		}
    return res;
  }

  async addLibrary(library: Library) {
    const db = await openDB('db', 1, onUpgradeNeeded);
    let tx = db.transaction(['collections', 'categories', 'items', 'settings', 'cache'], 'readwrite');

    this.addAll<Collection>(tx, library.collections, 'collections');
    this.addAll<Category>(tx, library.categories, 'categories');
    this.addAll<Item>(tx, library.items, 'items');
    this.addAll<Setting>(tx, library.settings, 'settings');
    this.addAll<CachedFile>(tx, library.cachedItems, 'cache');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  private getAll<T>(tx: IDBTransaction, objectStoreName: string) {
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
