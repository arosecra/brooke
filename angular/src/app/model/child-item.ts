import { BookDetails } from "./book-details";
import { VLCOptions } from "./vlc-options";

export declare interface ChildItem {
	name: string;
	collectionName: string;
	pathFromCategoryRoot: string;
	handle?: FileSystemFileHandle;
	series: boolean;
	vlcOptions?: VLCOptions;
	bookDetails?: BookDetails;
}