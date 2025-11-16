export class CRUD {
	
  static add<T>(tx: IDBTransaction, values: T, objectStoreName: string) {
    let req = tx.objectStore(objectStoreName);
		return new Promise<void>((resolve) => {
      	const request = req.put(values);
      	request.onsuccess = (e) => resolve();
			});
  }

  static addAll<T>(tx: IDBTransaction, values: T[], objectStoreName: string) {
		const requests: Promise<void>[] = [];
    let req = tx.objectStore(objectStoreName);
    for (let i = 0; i < values.length; i++) {
			requests.push(new Promise<void>((resolve) => {
      	const request = req.put(values[i]);
      	request.onsuccess = (e) => resolve();
			}));
    }
		return Promise.all(requests);
  }

  static getAll<T>(db: IDBDatabase, tx: IDBTransaction, storeName: string): Promise<T[]> {
    return new Promise<T[]>((resolve) => {
      const request = tx.objectStore(storeName).getAll();
      request.onsuccess = (e) => resolve(request.result as T[]);
    });
  }

  static remove(db: IDBDatabase, tx: IDBTransaction, storeName: string, key: IDBValidKey): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
      const request = tx.objectStore(storeName).delete(key);
      request.onsuccess = (e) => resolve(true);
    });
  }

  static removeAll(db: IDBDatabase, tx: IDBTransaction, storeName: string): Promise<boolean> {
    return new Promise<boolean>((resolve) => {
      const request = tx.objectStore(storeName).clear();
      request.onsuccess = (e) => resolve(true);
    });
  }
}
