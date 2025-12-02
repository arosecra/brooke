import { ItemRef } from './item-ref';


export declare interface Category {
	collectionName: string;
	name: string;
	displayName: string;
	items: ItemRef[];
}
