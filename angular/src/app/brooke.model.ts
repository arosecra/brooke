

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
    current: number;
    total: number;

}
