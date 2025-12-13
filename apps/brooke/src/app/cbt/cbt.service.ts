import { Injectable } from '@angular/core';
import { parseTarGzip } from 'nanotar';
import { ChildItem } from '../model/child-item';
import { Page } from '../model/page';
import { WebFS } from '../shared/web-fs';

@Injectable({
  providedIn: 'root',
})
export class CbtService {

  loadCbtGz(item: ChildItem): Promise<Page[]> {
    return new Promise(async (resolve, reject) => {
      let pages: Page[] = [];
      let pagesByName: Record<string, Page> = {};

      const webpFiles = item.handle ? await parseTarGzip(await WebFS.readBuffer(item.handle), { metaOnly: false }) : [];
      const thumbnailFiles = item.thumbsHandle ? await parseTarGzip(await WebFS.readBuffer(item.thumbsHandle), { metaOnly: false }) : [];
      const mdFiles = item.ocrHandle ? await parseTarGzip(await WebFS.readBuffer(item.ocrHandle), { metaOnly: false }) : [];

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
          .replaceAll('.webp', '')
          .replaceAll('.png', '');
        let base64 = await WebFS.bytesToBase64DataUrl(file.data);
        pagesByName[name].thumbnail = base64;
      }

      const decoder = new TextDecoder('utf-8');
      for (let i = 0; i < mdFiles.length; i++) {
        const file = mdFiles[i];
        if(file.name.endsWith('.md')) {
          let name = file.name.replace('md/', '').replaceAll('.md', '');
          pagesByName[name].markdown = decoder.decode(file.data);
        } else if (file.name.endsWith('_content_list.json')) {
          let name = file.name.replace('content_list/', '').replaceAll('_content_list.json', '');
          pagesByName[name].contentList = JSON.parse(decoder.decode(file.data));
        } else if (file.name.endsWith('_model.json')) {
          let name = file.name.replace('model/', '').replaceAll('_model.json', '');
          pagesByName[name].model = JSON.parse(decoder.decode(file.data));
        }
      }
      resolve(pages);
    });
  }
}
