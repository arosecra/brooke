import { BookDetails } from "./book-details";
import { VLCOptions } from "./vlc-options";

export declare interface ChildItem {
	name: string;
	collectionName: string;
	handle?: FileSystemFileHandle;
	ocrHandle?: FileSystemFileHandle;
	thumbsHandle?: FileSystemFileHandle;
	dirHandle: FileSystemDirectoryHandle;
	series: boolean;
	vlcOptions?: VLCOptions;
	bookDetails?: BookDetails;
}