export declare interface BrookeLocation {
  collection: Collection;
  category?: Category;
  series?: Item;
  item?: Item;
  leftPage?: string;
  rightPage?: string;
} 

export declare interface Collection {
  name: string;
  remoteDirectory: string;
  localDirectory: string;
  itemExtension: string;
  excludeExtensions: string[];
  openType: string;
  autoGenerateAlphaCategories: boolean;
  pipelineSteps: string[];
  categories: Category[];
}

export declare interface Category {
  name: string;
  items: Item[];
}

export declare interface CacheManifest {
  files: CachedFile[];
}

export declare interface CachedFile {
  collectionName: string;
  itemName: string;
  filename: string;
}

export declare interface Item {
  name: string;
  childItems: Item[];
  series?: boolean;
  vlcOptions?: VLCOptions;
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

export declare interface JobDetails {
  jobNumber: bigint;
  jobType: string;
  jobDescription: string;
  currentProgressDescription: string;
  totalProgressDescription: string;
  current: number;
  total: number;
}

export declare interface Page {
  elipses: boolean;
  page: string;
  current: boolean;
}

export declare interface MissingItem {
  collection: string;
  itemName: string;
  itemMissing: boolean;
}
