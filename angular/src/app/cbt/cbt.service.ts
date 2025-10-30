import { inject, Injectable } from '@angular/core';
import { parseTar } from "nanotar";
import { Files } from '../fs/library-fs';
import { CacheDirectory } from '../model/cache-directory';
import { Page } from '../model/page';
import { Collection } from '../model/collection';
import { Item } from '../model/item';
import { Library } from '../model/library';

@Injectable({
  providedIn: 'root',
})
export class CbtService {
	appFs = inject(Files);

	
  loadCbt(item: Item | undefined, cacheFileHandle: FileSystemFileHandle): Promise<Page[]> {
    return new Promise(async (resolve, reject) => {
      let pages: Page[] = [];
      let pagesByName: Record<string, Page> = {};

      const cacheFile = await cacheFileHandle.getFile();
      const cacheFileBuff = await cacheFile.arrayBuffer();
      const files = parseTar(cacheFileBuff, { metaOnly: false });

      let { webpFiles, thumbnailFiles, mdFiles } = this.separateFiles(files);

      for (let i = 0; i < webpFiles.length; i++) {
        const file = webpFiles[i];
        const basename = file.name.replaceAll('.webp', '').replaceAll('.png', '');
        let base64 = await this.appFs.bytesToBase64DataUrl(file.data);
        let page = {
          name: basename,
          fullPage: base64,
        } as Page;

        if (item?.bookDetails?.blankPages?.includes(basename)) {
          page.type = 'Blank';
        } else if (item?.bookDetails?.imagePages?.includes(basename)) {
          page.type = 'Image';
        } else if (item?.bookDetails?.excludePages?.includes(basename)) {
          page.type = 'Exclude';
        } else {
          page.type = 'Text';
        }

        pages.push(page);
        pagesByName[basename] = page;
      }

      for (let i = 0; i < thumbnailFiles.length; i++) {
        const file = thumbnailFiles[i];
        let name = file.name.replace('.thumbnails/', '').replaceAll('.webp', '').replaceAll('.png', '');
        let base64 = await this.appFs.bytesToBase64DataUrl(file.data);
        pagesByName[name].thumbnail = base64;
      }

      const decoder = new TextDecoder('utf-8');
      for (let i = 0; i < mdFiles.length; i++) {
        const file = mdFiles[i];
        let name = file.name.replace('.md/', '').replaceAll('.md', '');
        pagesByName[name].markdown = decoder.decode(file.data);
      }
      resolve(pages);
    });
  }

	
	separateFiles(files: any[]) {
		let mdFiles = [];
		let thumbnailFiles = [];
		let webpFiles = [];

		for(let i = 0; i < files.length; i++) {
			const file = files[i];
			if(file.data) {
				if(file.name.startsWith('.md')) {
					mdFiles.push(file);
				} else if(file.name.startsWith('.thumbnails')) {
					thumbnailFiles.push(file);
				} else {
					webpFiles.push(file);
				}
			}
		}
		return {webpFiles, thumbnailFiles, mdFiles};
	}
}
