import { BookDetails } from './BookDetails';
import { Category } from './Category';
import { Collection } from './Collection';
import { CompleteItem } from './CompleteItem';

export declare interface Location {
  collection: Collection;
  category: Category;
  collectionCategories: Category[];
  completeItems: CompleteItem[];
  series: CompleteItem;
  item: CompleteItem;
  pageSet: number;
  bookDetails: BookDetails;
  search: string;
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
    search: '',
  };
}
