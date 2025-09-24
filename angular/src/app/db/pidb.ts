export function openDB(
  dbName: string,
  version: number,
  onUpgradeNeeded: (this: IDBOpenDBRequest, ev: IDBVersionChangeEvent) => any,
): Promise<IDBDatabase> {
  return new Promise<IDBDatabase>((resolve) => {
    const openRequest = indexedDB.open(dbName, version);
    openRequest.onupgradeneeded = onUpgradeNeeded;
    openRequest.onsuccess = function () {
      let db = openRequest.result;

      db.onversionchange = function () {
        db.close();
        alert('Database is outdated, please reload the page.');
      };

      resolve(db);
    };

    openRequest.onblocked = function () {
      // this event shouldn't trigger if we handle onversionchange correctly
      // it means that there's another open connection to the same database
      // and it wasn't closed after db.onversionchange triggered for it
    };
  });
}

export function getAll<T>(tx: IDBTransaction, objectStoreName: string) {
  return new Promise((resolve) => {
    const request = tx.objectStore(objectStoreName).getAll();
    request.onsuccess = (e) => resolve(request.result as T);
  });
}
