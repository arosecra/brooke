import { Item } from './Item';
import { Category } from './Category';
import { Collection } from './Collection';

export declare interface LibraryOptions {
  collections: Collection[];
  categories: Category[];
  items: Item[];
}
