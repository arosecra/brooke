export abstract class CRUD<T> {
  addAll(db: IDBDatabase, values: T[], tx: IDBTransaction) {
    let req = tx.objectStore(this.getStoreName());
    for (let i = 0; i < values.length; i++) {
      const t = values[i];
      req.put(t);
    }
  }

  getAll(db: IDBDatabase, tx: IDBTransaction): Promise<T[]> {
    return new Promise<T[]>((resolve) => {
      const request = tx.objectStore(this.getStoreName()).getAll();
      request.onsuccess = (e) => resolve(request.result as T[]);
    });
  }

  remove(db: IDBDatabase, tx: IDBTransaction, key: IDBValidKey): Promise<T[]> {
    return new Promise<T[]>((resolve) => {
      const request = tx.objectStore(this.getStoreName()).delete(key);
      request.onsuccess = (e) => resolve([] as T[]);
    });
  }

  removeAll(db: IDBDatabase, tx: IDBTransaction): Promise<T[]> {
    return new Promise<T[]>((resolve) => {
      const request = tx.objectStore(this.getStoreName()).clear();
      request.onsuccess = (e) => resolve([] as T[]);
    });
  }

  abstract getStoreName(): string;
}
