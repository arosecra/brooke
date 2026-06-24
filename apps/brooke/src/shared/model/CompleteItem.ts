import { Item } from './Item';
import { ItemRef } from './ItemRef';
import { Thumbnail } from './Thumbnail';

export interface CompleteItem {
  item: Item;
  itemRef: ItemRef;
  thumbnail: Thumbnail;
}
