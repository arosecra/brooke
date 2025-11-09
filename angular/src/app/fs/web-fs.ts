

export type WebFSPermission = 'readwrite' | 'read';

export class WebFS {

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

  static async getFileHandle(handle: FileSystemDirectoryHandle, arg1: string, arg2?: any) {
    try {
      let parts = arg1.split('/').filter((val) => val !== '');
      let currentHandle = handle;
      for (let i = 0; i < parts.length - 1; i++) {
        currentHandle = await currentHandle.getDirectoryHandle(parts[i]);
      }
      return await currentHandle.getFileHandle(parts[parts.length - 1], arg2);
    } catch (err) {
      return null;
    }
  }
	
  static async getDirectoryHandle(handle: FileSystemDirectoryHandle, arg1: string) {
    let parts = arg1.split('/').filter((val) => val !== '');
    let currentHandle = handle;
    for (let i = 0; i < parts.length; i++) {
      currentHandle = await currentHandle.getDirectoryHandle(parts[i]);
    }
    return currentHandle;
  }

	
}