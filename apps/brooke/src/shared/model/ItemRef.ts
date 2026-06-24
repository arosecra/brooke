import { ChildItemRef } from './ChildItemRef';

export declare interface ItemRef extends ChildItemRef {
  series: boolean;
  childItems: ChildItemRef[];
}
