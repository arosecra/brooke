import { Category } from './category';


export declare interface Collection {
	name: string;
	displayName: string;
	directory: string;
	handle: FileSystemDirectoryHandle;
	hasPermission?: boolean;
	itemExtension: string;
	excludeExtensions: string[];
	openType: string;
	autoGenerateAlphaCategories: boolean;
	pipelineSteps: string[];
	categories: Category[];
}
