import { ToCEntry } from './toc-entry';


export declare interface BookDetails {
	numberOfPages: number;
	rawSize: number;
	cbtSize: number;
	compression: number;

	tocEntries: ToCEntry[];
	
	imagePages?: string[];
	excludePages?: string[];
	blankPages?: string[];
}
