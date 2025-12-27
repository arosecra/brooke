import { CachedFile } from './cached-file';
import { Item } from './item';
import { Setting } from './setting';
import { Category } from './category';
import { Collection } from './collection';
import { Thumbnail } from './thumbnail';


export declare interface LibraryOptions {
	collections: Collection[];
	categories: Category[];
	items: Item[];
}
