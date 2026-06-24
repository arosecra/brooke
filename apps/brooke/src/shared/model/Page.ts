import { PageType } from './PageType';

export declare interface Page {
  name: string;
  markdown: string;
  model: any;
  contentList: any;
  thumbnail: string;
  fullPage: string;
  type: PageType;
  bookmarkName?: string;
}
