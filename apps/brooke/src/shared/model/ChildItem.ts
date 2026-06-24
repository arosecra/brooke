import { BookDetails } from './BookDetails';
import { VLCOptions } from './VLCOptions';

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
