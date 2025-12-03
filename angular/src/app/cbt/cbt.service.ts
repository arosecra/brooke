import { Injectable } from '@angular/core';
import { parseTarGzip } from 'nanotar';
import { ChildItem } from '../model/child-item';
import { Page } from '../model/page';
import { WebFS } from '../shared/web-fs';

@Injectable({
  providedIn: 'root',
})
export class CbtService {

  loadCbtGz(item: ChildItem | undefined, cacheFileHandle: FileSystemFileHandle): Promise<Page[]> {
    return new Promise(async (resolve, reject) => {
      let pages: Page[] = [];
      let pagesByName: Record<string, Page> = {};

      const cacheFile = await cacheFileHandle.getFile();
      const cacheFileBuff = await cacheFile.arrayBuffer();
      const files = await parseTarGzip(cacheFileBuff, { metaOnly: false });

      let { webpFiles, thumbnailFiles, mdFiles } = this.separateFiles(files);

      for (let i = 0; i < webpFiles.length; i++) {
        const file = webpFiles[i];
        const basename = file.name.replaceAll('.webp', '').replaceAll('.png', '');
        let base64 = await WebFS.bytesToBase64DataUrl(file.data);
        let page = {
          name: basename,
          fullPage: base64,
        } as Page;

        if (item?.bookDetails?.blankPages?.includes(basename)) {
          page.type = 'Blank';
        } else if (item?.bookDetails?.imagePages?.includes(basename)) {
          page.type = 'Image';
        } else {
          page.type = 'Text';
        }

        pages.push(page);
        pagesByName[basename] = page;
      }

      if (item?.bookDetails?.tocEntries) {
        for (let i = 0; i < item.bookDetails.tocEntries.length; i++) {
          let tocEntry = item.bookDetails.tocEntries[i];
          pages[tocEntry.pageNumber].bookmarkName = tocEntry.name;
        }
      }

      for (let i = 0; i < thumbnailFiles.length; i++) {
        const file = thumbnailFiles[i];
        let name = file.name
          .replace('.thumbnails/', '')
          .replaceAll('.webp', '')
          .replaceAll('.png', '');
        let base64 = await WebFS.bytesToBase64DataUrl(file.data);
        pagesByName[name].thumbnail = base64;
      }

      const decoder = new TextDecoder('utf-8');
      for (let i = 0; i < mdFiles.length; i++) {
        const file = mdFiles[i];
        let name = file.name.replace('.ocr/md/', '').replaceAll('.md', '');
        pagesByName[name].markdown = decoder.decode(file.data);
      }
      resolve(pages);
    });
  }

  separateFiles(files: any[]) {
    let mdFiles = [];
    let thumbnailFiles = [];
    let webpFiles = [];

    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      if (file.data) {
        if (file.name.startsWith('.ocr')) {
          if (file.name.endsWith('.md')) mdFiles.push(file);
        } else if (file.name.startsWith('.thumbnails')) {
          thumbnailFiles.push(file);
        } else {
          webpFiles.push(file);
        }
      }
    }
    return { webpFiles, thumbnailFiles, mdFiles };
  }
}
