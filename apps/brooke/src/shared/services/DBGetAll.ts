

export function dbGetAll<T>(tx: IDBTransaction, objectStoreName: string) {
  return new Promise((resolve) => {
    const request = tx.objectStore(objectStoreName).getAll();
    request.onsuccess = (e) => resolve(request.result as T);
  });
}