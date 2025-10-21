import { NewCollection, NewCategory, Item, Setting, CachedFile } from '../app-model';


export declare interface LibraryOptions {
	collections: NewCollection[];
	categories: NewCategory[];
	items: Item[];
	settings: Setting[];
	cachedItems: CachedFile[];
}
