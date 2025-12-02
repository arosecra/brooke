import { ChildItem } from './child-item';


export declare interface Item extends ChildItem {
	childItems: ChildItem[];
}
