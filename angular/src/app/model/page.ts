
export declare interface Page {
	name: string;
	markdown: string;
	thumbnail: string;
	fullPage: string;
	type: PageType;
	bookmarkName?: string;
}

export declare type PageType = 'Text' | 'Image' | 'Blank' | 'Exclude';
