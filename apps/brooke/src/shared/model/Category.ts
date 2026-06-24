import { ItemRef } from './ItemRef';

export declare interface Category {
  collectionName: string;
  name: string;
  displayName: string;
  synthetic: boolean;
  alphabetical: boolean;
  items: ItemRef[];
}
