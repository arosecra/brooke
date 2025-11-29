import YAML from 'yaml';


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
    sourceHandle: FileSystemFileHandle, toDir: FileSystemDirectoryHandle
  ) {
    const outputPermission = await WebFS.getPermission(toDir, 'readwrite');
    const inputPermission = await WebFS.getPermission(sourceHandle, 'read');

    if (inputPermission && outputPermission) {
			let cacheFile = await toDir.getFileHandle(sourceHandle.name, { create: true });
			let remoteFile = await sourceHandle.getFile();

			if(cacheFile && remoteFile) {
				const fileContent = await remoteFile.arrayBuffer();
				const writableStream = await cacheFile.createWritable();
				await writableStream.write(fileContent);
				writableStream.close();
			}
    }
    return true;
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

	static async readText(file: FileSystemFileHandle) {
		let f = await file.getFile();
		return f.text();
	}

	static async readYaml<T>(file: FileSystemFileHandle) {
		return YAML.parse(await WebFS.readText(file)) as T;
	}

	static async bytesToBase64DataUrl(bytes: any, type = "image/webp"): Promise<string> {
		return await new Promise((resolve, reject) => {
			const reader = Object.assign(new FileReader(), {
				onload: () => resolve(reader.result as string),
				onerror: () => reject(reader.error),
			});
			reader.readAsDataURL(new File([bytes], "", { type }));
		});
	}
}