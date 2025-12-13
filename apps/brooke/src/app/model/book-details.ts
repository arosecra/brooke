import { ToCEntry } from './toc-entry';


export declare interface BookDetails {
	tocEntries: ToCEntry[];
	
	imagePages?: string[];
	blankPages?: string[];
}
