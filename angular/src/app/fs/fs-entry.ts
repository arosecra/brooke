export declare interface FSEntry {
  name: string;
  path: string;
  parentPath: string;
  handle: FileSystemFileHandle | FileSystemDirectoryHandle;
}
