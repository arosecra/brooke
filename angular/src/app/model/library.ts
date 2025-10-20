import { NewCollection, NewCategory, Item, Setting, CachedFile, CacheDirectory } from '../app-model';

export declare interface LibraryOptions {
  collections: NewCollection[];
  categories: NewCategory[];
  items: Item[];
  settings: Setting[];
	cachedItems: CachedFile[];
}

export class Library {
  collections: NewCollection[];
  categories: NewCategory[];
  items: Item[];
  settings: Setting[];
	cachedItems: CachedFile[];
	cacheDirectory: CacheDirectory | null = null;

  itemsByCollectionAndName: Record<string, Item> = {};
	settingsByName: Record<string, any> = {};

  constructor(options?: LibraryOptions) {
    this.collections = options?.collections ? [...options.collections] : [];
    this.categories = options?.categories ? [...options.categories] : [];
    this.items = options?.items ? [...options.items] : [];
    this.settings = options?.settings ? [...options.settings] : [];
    this.cachedItems = options?.cachedItems ? [...options.cachedItems] : [];

    for (let i = 0; options && i < options.items.length; i++) {
      const item = options.items[i];
      this.itemsByCollectionAndName[item.collectionName + '_' + item.name] = item;
    }

    for (let i = 0; options && i < options.settings.length; i++) {
      const setting = options.settings[i];
      this.settingsByName[setting.name] = setting;
    }
  }

  private copyRecord<T>(rec: Record<string, T[]> | undefined) {
    let res: Record<string, T[]> = {};
    if (rec) Object.entries(rec).forEach(([key, value]) => (res[key] = [...value]));
    return res;
  }

  addCollection(collection: NewCollection): Library {
    return new Library({
      collections: [...this.collections, collection],
      categories: this.categories,
      items: this.items,
      settings: this.settings,
			cachedItems: this.cachedItems,
    });
  }

  addCategory(category: NewCategory): Library {
    return new Library({
      collections: this.collections,
      categories: [...this.categories, category],
      items: this.items,
      settings: this.settings,
			cachedItems: this.cachedItems,
    });
  }
}
