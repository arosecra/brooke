import { ChildItemRef } from "./child-item-ref";

export declare interface ItemRef extends ChildItemRef {
	series: boolean;
	childItems: ChildItemRef[];
}
