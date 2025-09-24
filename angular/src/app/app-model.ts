import { Signal } from '@angular/core';

export declare interface NewCollection {
  name: string;
  directory: string;
  handle: FileSystemDirectoryHandle;
  hasPermission?: boolean;
  itemExtension: string;
  excludeExtensions: string[];
  openType: string;
  autoGenerateAlphaCategories: boolean;
  pipelineSteps: string[];
  categories: NewCategory[];
}

export declare interface NewCategory {
  collectionName: string;
  name: string;
  items: ItemRef[];
}

export declare interface Setting {
  name: string;
  value: any;
}

export declare interface Item {
  name: string;
  collectionName: string;
  pathFromCategoryRoot: string;
  thumbnail?: string;
  largeThumbnail?: string;
  series?: boolean;
  vlcOptions?: VLCOptions;
  bookDetails?: BookDetails;
  childItems?: Item[];
}

export declare interface CachedFile {
  collectionName: string;
  itemName: string;
  filename: string;
}

export declare interface ItemRef {
  name: string;
  series?: boolean;
  childItems?: ItemRef[];
}

export declare interface VLCOptions {
  audioTrack: number;
  subtitleTrack: number;
}

export declare interface BookDetails {
  numberOfPages: number;
  rawSize: number;
  cbtSize: number;
  compression: number;

  tocEntries: ToCEntry[];
}

export declare interface ToCEntry {
  name: string;
  pageNumber: number;
}

export declare interface Page {
  bytes: Signal<any | undefined>;
  pageNo: number;
}
