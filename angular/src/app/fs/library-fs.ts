import { Injectable } from '@angular/core';
import { FSEntry } from './fs-entry';
import YAML from 'yaml';
import { Item } from '../model/item';
import { Collection } from '../model/collection';
import { Library } from '../model/library';

@Injectable({
  providedIn: 'root',
})
export class Files {
	async cacheFile(library: Library, collection: Collection, item: Item) {
		let cacheDirectory = library?.settingsByName['cacheDirectory'].value as FileSystemDirectoryHandle;

		let cdPermission = await this.hasReadWritePermission(cacheDirectory);

		if(!cdPermission) {
			await this.getReadWritePermission(cacheDirectory);
		}
		let remoteFileHandle = await this.getFileHandle(collection.handle, item.pathFromCategoryRoot + '/' + item.name + '.' + collection.itemExtension);
		let cacheFile = await cacheDirectory.getFileHandle(item.name + '.' + collection.itemExtension, { create: true });
		let remoteFile = await remoteFileHandle?.getFile();
		// let rfPermission = await this.hasReadWritePermission(remoteFile);

		if(cacheFile && remoteFile) {
			const fileContent = await remoteFile.arrayBuffer();
			const writableStream = await cacheFile.createWritable();
			await writableStream.write(fileContent);
			writableStream.close();
		}
		return cacheFile?.name;
	}
  async getFileHandle(handle: FileSystemDirectoryHandle, arg1: string) {
    try {
      let parts = arg1.split('/').filter((val) => val !== '');
      let currentHandle = handle;
      for (let i = 0; i < parts.length - 1; i++) {
        currentHandle = await currentHandle.getDirectoryHandle(parts[i]);
      }
      return await currentHandle.getFileHandle(parts[parts.length - 1]);
    } catch (err) {
      return null;
    }
  }
  async getDirectoryHandle(handle: FileSystemDirectoryHandle, arg1: string) {
    let parts = arg1.split('/').filter((val) => val !== '');
    let currentHandle = handle;
    for (let i = 0; i < parts.length; i++) {
      currentHandle = await currentHandle.getDirectoryHandle(parts[i]);
    }
    return currentHandle;
  }
  getCollectionForFile() {}

  async hasReadWritePermission(handle: any) {
    return (await handle.queryPermission({ mode: 'readwrite' })) === 'granted';
  }

  async getReadWritePermission(handle: any) {
    if (!(await this.hasReadWritePermission(handle))) {
      const permission = await handle.requestPermission({ mode: 'readwrite' });

      if (permission !== 'granted') {
        throw new Error('No permission to open file');
      }
    }

    return true;
  }

	async bytesToBase64DataUrl(bytes: any, type = "image/webp"): Promise<string> {
		return await new Promise((resolve, reject) => {
			const reader = Object.assign(new FileReader(), {
				onload: () => resolve(reader.result as string),
				onerror: () => reject(reader.error),
			});
			reader.readAsDataURL(new File([bytes], "", { type }));
		});
	}

  async getFiles(directoryHandle: FileSystemDirectoryHandle) {
    return this._iterateFiles(directoryHandle, '', {});
  }

  async _iterateFiles(
    directoryHandle: FileSystemDirectoryHandle,
    parentPath: string,
    acc: Record<string, FSEntry> = {},
  ) {
    for await (const [name, handle] of directoryHandle.entries()) {
      const path = `${parentPath}/${name}`;

      let entry: FSEntry = {
        name,
        path,
        handle,
        parentPath,
      };

      acc[path] = entry;

      if (handle.kind === 'directory') {
        await this._iterateFiles(handle, path, acc);
      }
    }

    return acc;
  }

  getParentDirectoryForDirectoryPath(path: string) {
    let parts = path.split('/').filter((val) => val !== '');
    parts.splice(parts.length - 1, 1);
    return '/' + parts.join('/');
  }

  async getImageFileContents(file: FileSystemFileHandle): Promise<File> {
    return await file.getFile();
    // return new Promise((resolve, reject) => {
    //   const reader = new FileReader();

    //   reader.addEventListener('loadend', () => resolve(reader.result as string));
    //   reader.readAsArrayBuffer(f);
    // });
  }

  async getTextFileContents(file: FileSystemFileHandle) {
    let f = await file.getFile();
    return f.text();
  }

  async getYAMLFileContents<T>(file: FileSystemFileHandle) {
    return YAML.parse(await this.getTextFileContents(file)) as T;
  }

	
}
