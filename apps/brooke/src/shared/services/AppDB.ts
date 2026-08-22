import { Category } from '../model/Category';
import { ChildItem } from '../model/ChildItem';
import { Collection } from '../model/Collection';
import { Item } from '../model/Item';
import { Library } from '../model/Library';
import { Setting } from '../model/Setting';
import { Settings } from '../model/Settings';
import { Thumbnail } from '../model/Thumbnail';
import { dbOpen } from './DBOpen';
import { Orator } from './Orator';
import { CRUD } from './WebCRUD';
import { WebFS } from './WebFS';

export function onUpgradeNeeded(
  this: IDBOpenDBRequest,
  event: IDBVersionChangeEvent,
) {
  let db = this.result;
  switch (event.oldVersion) {
    case 0:
      db.createObjectStore('collections', {
        keyPath: 'name',
      });
      db.createObjectStore('categories', {
        keyPath: ['collectionName', 'name'],
      });
      db.createObjectStore('items', {
        keyPath: ['collectionName', 'name'],
      });
      db.createObjectStore('settings', { keyPath: 'name' });
      db.createObjectStore('thumbnails', {
        keyPath: [
          'collectionName',
          'categoryName',
          'itemName',
        ],
      });
      break;
    case 1:
      break;
  }
}

export const TABLE_NAMES = [
  'collections',
  'categories',
  'items',
  'settings',
  'thumbnails',
];

export const DB_NAME = 'brookedb';

export class AppDB {
  orator: Orator;

  constructor(orator: Orator) {
    this.orator = orator;
  }

  async getSettings(): Promise<Settings> {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readonly');

    const settingsArr = await this.getAll<Setting[]>(
      tx,
      'settings',
    );
    return {
      cacheDirectory: settingsArr.find(
        (val) => val.name === 'cacheDirectory',
      )?.value,
      voice:
        settingsArr?.find((val) => val.name === 'voice')
          ?.value ??
        this.orator
          .getVoices()
          .find(
            (voice: SpeechSynthesisVoice) => voice.default,
          )?.name,
      defaultPagesPer: !!settingsArr?.find(
        (val) => val.name === 'defaultPagesPer',
      )?.value,
    };
  }

  async getLibrary() {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readonly');

    let res = new Library({
      collections: await this.getAll<Collection[]>(
        tx,
        'collections',
      ),
      categories: await this.getAll<Category[]>(
        tx,
        'categories',
      ),
      items: await this.getAll<Item[]>(tx, 'items'),
    });
    for (let i = 0; i < res.collections.length; i++) {
      res.collections[i].hasRWPermission =
        await WebFS.hasPermission(
          res.collections[i].handle,
          'readwrite',
        );
      res.collections[i].hasRPermission =
        await WebFS.hasPermission(
          res.collections[i].handle,
          'read',
        );
    }

    return res;
  }

  async getCollections() {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readonly');
    return this.getAll<Collection[]>(tx, 'collections');
  }

  async addLibrary(library: Library) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Collection>(
      tx,
      library.collections,
      'collections',
    );
    this.addAll<Category>(
      tx,
      library.categories,
      'categories',
    );
    this.addAll<Item>(tx, library.items, 'items');

    tx.commit();

    return new Promise<boolean>((resolve, reject) => {
      tx.oncomplete = (e) => resolve(true);
      tx.onerror = (e) => {
        alert(JSON.stringify(e));
        reject(e);
      };
      tx.onabort = (e) => {
        alert('aborted' + JSON.stringify(e));
        reject(e);
      };
    });
  }

  async addCollection(collection: Collection) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Collection>(
      tx,
      [collection],
      'collections',
    );

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async addCategories(category: Category[]) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Category>(tx, category, 'categories');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async addCategory(category: Category) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    CRUD.add<Category>(tx, category, 'categories');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async addItems(items: Item[]) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Item>(tx, items, 'items');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async addItem(item: Item | ChildItem) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    await CRUD.add<Item | ChildItem>(tx, item, 'items');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async addThumbnail(thumbnail: Thumbnail) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    await CRUD.add<Thumbnail>(tx, thumbnail, 'thumbnails');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async addThumbnails(thumbnails: Thumbnail[]) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    return new Promise<boolean>((resolve, reject) => {
      tx.oncomplete = (e) => resolve(true);
      tx.onerror = (e) => {
        alert(JSON.stringify(e));
        reject(false);
      };
      tx.onabort = (e) => {
        alert('aborted' + JSON.stringify(e));
        reject(false);
      };

      this.addAll<Thumbnail>(tx, thumbnails, 'thumbnails');
      tx.commit();
    });
  }

  async addSetting(setting: Setting) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    this.addAll<Setting>(tx, [setting], 'settings');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async updateSettings(settings: Settings) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    const settingsArr = Object.entries(settings).map(
      (s) => ({
        name: s[0],
        value: s[1],
      }),
    );

    await this.addAll<Setting>(tx, settingsArr, 'settings');

    tx.commit();

    return new Promise<boolean>((resolve) => {
      tx.oncomplete = (e) => resolve(true);
    });
  }

  async removeCollection(collection: Collection) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
    await this.remove(tx, 'collections', collection.name);
  }

  async removeCategories(collection: Collection) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
    const lowerBoundKey = [collection.name];
    const upperBoundKey = [collection.name, []];
    const keyRange = IDBKeyRange.bound(
      lowerBoundKey,
      upperBoundKey,
    );
    await this.remove(tx, 'categories', keyRange);
  }

  async removeItems(collection: Collection) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
    const lowerBoundKey = [collection.name];
    const upperBoundKey = [collection.name, []];
    const keyRange = IDBKeyRange.bound(
      lowerBoundKey,
      upperBoundKey,
    );
    await this.remove(tx, 'items', keyRange);
  }

  async removeThumbnails(collection: Collection) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
    const lowerBoundKey = [collection.name];
    const upperBoundKey = [collection.name, [], []];
    const keyRange = IDBKeyRange.bound(
      lowerBoundKey,
      upperBoundKey,
    );
    await this.remove(tx, 'thumbnails', keyRange);
  }

  async getCategoriesForCollection(collectionName: string) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    const tx = db.transaction(TABLE_NAMES, 'readwrite');
    const lowerBoundKey = [collectionName];
    const upperBoundKey = [collectionName, []];
    const keyRange = IDBKeyRange.bound(
      lowerBoundKey,
      upperBoundKey,
    );
    return new Promise<Category[]>((resolve) => {
      const request = tx
        .objectStore('categories')
        .getAll(keyRange);
      request.onsuccess = (e) => {
        const r: Category[] = request.result.map(
          (category) => category,
        );
        resolve(r);
      };
    });
  }

  async getItemsThatMatch(search: string) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');

    return new Promise<Item[]>((resolve, reject) => {
      const res: Item[] = [];
      const request = tx.objectStore('items').openCursor();
      request.onsuccess = (e) => {
        const target =
          e.target as IDBRequest<IDBCursorWithValue | null>;
        const cursor = target.result;

        if (cursor) {
          if (cursor.value.name.includes(search)) {
            // const primaryKey = cursor.primaryKey;
            // const key = cursor.key;
            // const value = cursor.value;
            // console.log(primaryKey, key, value);
            res.push(cursor.value);
          }

          cursor.continue();
        }
      };
      request.onerror = () => reject(request.error);
      tx.oncomplete = () => resolve(res);
    });
  }

  async getItemsForCollection(collectionName: string) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
    const lowerBoundKey = [collectionName];
    const upperBoundKey = [collectionName, []];
    const keyRange = IDBKeyRange.bound(
      lowerBoundKey,
      upperBoundKey,
    );
    return new Promise<Item[]>((resolve) => {
      const request = tx
        .objectStore('items')
        .getAll(keyRange);
      request.onsuccess = (e) => {
        const r: Item[] = request.result.map(
          (item) => item,
        );
        resolve(r);
      };
    });
  }

  async getThumbnailsForCollectionAndItems(
    collectionName: string,
    itemNames: Set<string>,
  ) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
    const lowerBoundKey = [collectionName];
    const upperBoundKey = [collectionName, []];
    const keyRange = IDBKeyRange.bound(
      lowerBoundKey,
      upperBoundKey,
    );
    return new Promise<Record<string, Thumbnail>>(
      (resolve) => {
        const request = tx
          .objectStore('thumbnails')
          .getAll(keyRange);
        request.onsuccess = (e) => {
          const r: Record<string, Thumbnail> = {};
          request.result.forEach((thumbnail) => {
            if (itemNames.has(thumbnail.itemName))
              r[thumbnail.itemName] = thumbnail;
          });
          resolve(r);
        };
      },
    );
  }

  async getThumbnailsForCollectionAndCategory(
    collectionName: string,
    categoryName: string,
  ) {
    const db = await dbOpen(DB_NAME, 1, onUpgradeNeeded);
    let tx = db.transaction(TABLE_NAMES, 'readwrite');
    const lowerBoundKey = [collectionName, categoryName];
    const upperBoundKey = [
      collectionName,
      categoryName,
      [],
    ];
    const keyRange = IDBKeyRange.bound(
      lowerBoundKey,
      upperBoundKey,
    );
    return new Promise<Record<string, Thumbnail>>(
      (resolve) => {
        const request = tx
          .objectStore('thumbnails')
          .getAll(keyRange);
        request.onsuccess = (e) => {
          const r: Record<string, Thumbnail> =
            request.result.reduce((acc, thumbnail) => {
              acc[thumbnail.itemName] = thumbnail;
              return acc;
            }, {});
          resolve(r);
        };
      },
    );
  }

  remove(
    tx: IDBTransaction,
    objectStoreName: string,
    key: IDBKeyRange | IDBValidKey,
  ): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
      const request = tx
        .objectStore(objectStoreName)
        .delete(key);
      request.onsuccess = (e) => resolve(true);
    });
  }

  getAll<T>(tx: IDBTransaction, objectStoreName: string) {
    return new Promise<T>((resolve) => {
      const request = tx
        .objectStore(objectStoreName)
        .getAll();
      request.onsuccess = (e) =>
        resolve(request.result as T);
    });
  }

  addAll<T>(
    tx: IDBTransaction,
    values: T[],
    objectStoreName: string,
  ) {
    const requests: Promise<void>[] = [];
    let req = tx.objectStore(objectStoreName);
    for (let i = 0; i < values.length; i++) {
      requests.push(
        new Promise<void>((resolve, reject) => {
          const request = req.put(values[i]);
          request.onsuccess = (e) => resolve();
          request.onerror = (e) => reject(e);
        }),
      );
    }
    return Promise.all(requests);
  }
}
