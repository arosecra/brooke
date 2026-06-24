import { BookDetails } from './BookDetails';
import { Category } from './Category';
import { ChildItem } from './ChildItem';
import { Collection } from './Collection';
import { CompleteItem } from './CompleteItem';
import { ItemRef } from './ItemRef';

export declare interface Location {
  collection: Collection;
  category: Category;
  collectionCategories: Category[];
  completeItems: CompleteItem[];
  series: ItemRef;
  item: ChildItem;
  pageSet: number;
  bookDetails: BookDetails;
}

export function location() {
  return {
    collection: null,
    category: null,
    collectionCategories: null,
    completeItems: null,
    item: null,
    series: null,
    bookDetails: null,
    pageSet: 0,
  };
}
