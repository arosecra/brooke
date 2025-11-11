import { VLCOptions } from './vlc-options';
import { BookDetails } from './book-details';


export declare interface Item {
	name: string;
	collectionName: string;
	pathFromCategoryRoot: string;
	handle?: FileSystemFileHandle;
	thumbnail?: File;
	largeThumbnail?: string;
	series: boolean;
	vlcOptions?: VLCOptions;
	bookDetails?: BookDetails;
	childItems: Item[];
}
