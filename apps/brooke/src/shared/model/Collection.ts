import { Category } from './Category';

export declare interface Collection {
  name: string;
  displayName: string;
  directory: string;
  handle: FileSystemDirectoryHandle;
  hasRPermission: boolean;
  hasRWPermission: boolean;
  itemExtension: string;
  excludeExtensions: string[];
  openType: string;
  autoGenerateAlphaCategories: boolean;
  pipelineSteps: string[];
  categories: Category[];
}
