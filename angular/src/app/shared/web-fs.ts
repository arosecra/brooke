;import { FSEntry } from "../fs/fs-entry";


export type WebFSPermission = 'readwrite' | 'read';

export class WebFS {

	static async onLeafDirs(
		baseDir: FileSystemDirectoryHandle, 
		callback: (index: number, parent: FileSystemDirectoryHandle, leaf: FileSystemDirectoryHandle) => Promise<any>,
		parent: FileSystemDirectoryHandle,
		index: number
	) {
		const children = await WebFS.readdir(baseDir);
		const iAmLeaf = !children.some((child) => child.kind === 'directory');

		if(iAmLeaf) {
			await callback(index, parent, baseDir);
		} else {
			for(let i = 0; i < children.length; i++) {
				let child = children[i];
				if(child.kind === 'directory') {
					await WebFS.onLeafDirs(child, callback, baseDir, i);
				}
			}
		}
		return Promise.resolve(true);
	}

	static async readdir(baseDir: FileSystemDirectoryHandle,
		options?: { recursive: false }
	): Promise<(FileSystemDirectoryHandle | FileSystemFileHandle)[]> {
		let files: (FileSystemDirectoryHandle | FileSystemFileHandle)[] = [];
		for await (const [name, handle] of baseDir.entries()) {
      files.push(handle);

			if(options?.recursive) {
				if(handle.kind === 'directory') {
					files = [...files, ...await WebFS.readdir(handle, options)];
				}
			}
    }
		return files;
	}

  static async copyFile(
    inputBaseDir: FileSystemDirectoryHandle,
    inputPath: string,
    outputBaseDir: FileSystemDirectoryHandle,
    outputPath: string,
  ) {
    const outputPermission = await WebFS.getPermission(outputBaseDir, 'readwrite');
    const inputPermission = await WebFS.getPermission(inputBaseDir, 'read');

    if (inputPermission && outputPermission) {
      let inputFileHandle = await WebFS.getFileHandle(inputBaseDir, inputPath);
      let outputFile = await WebFS.getFileHandle(outputBaseDir, outputPath, { create: true });
      let inputFile = await inputFileHandle?.getFile();

      if (outputFile && inputFile) {
        const fileContent = await inputFile.arrayBuffer();
        const writableStream = await outputFile.createWritable();
        await writableStream.write(fileContent);
        writableStream.close();
        return true;
      }
    }
    return false;
  }

  static async hasPermission(handle: any, permission: WebFSPermission) {
    return (await handle.queryPermission({ mode: permission })) === 'granted';
  }

  static async getPermission(handle: any, permission: WebFSPermission) {
    if (!(await WebFS.hasPermission(handle, permission))) {
      const result = await handle.requestPermission({ mode: permission });

      if (result !== 'granted') {
        return false;
      }
    }

    return true;
  }

  static async getFileHandle(handle: FileSystemDirectoryHandle, baseDir: string, path?: any) {
    try {
      let parts = baseDir.split('/').filter((val) => val !== '');
      let currentHandle = handle;
      for (let i = 0; i < parts.length - 1; i++) {
        currentHandle = await currentHandle.getDirectoryHandle(parts[i]);
      }
      return await currentHandle.getFileHandle(parts[parts.length - 1], path);
    } catch (err) {
      return null;
    }
  }
	
  static async getDirectoryHandle(handle: FileSystemDirectoryHandle, relativePath: string) {
    let parts = relativePath.split('/').filter((val) => val !== '');
    let currentHandle = handle;
    for (let i = 0; i < parts.length; i++) {
      currentHandle = await currentHandle.getDirectoryHandle(parts[i]);
    }
    return currentHandle;
  }

	
}