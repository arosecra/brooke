import { ViewportScroller } from '@angular/common';
import {
  AfterViewInit,
  Component,
  inject,
} from '@angular/core';
import { AppContextProvider } from '../../../providers/app-context-provider';
import { Page, PageType } from '../../../shared';
import { Card } from '../cards/card/card';
import { CardBody } from '../cards/card/card-body';
import { CardActions } from '../cards/card/card-actions';
import { ButtonRadioGroupComponent } from '../../../shared/widgets/button-radio-group';

@Component({
  selector: 'thumbnail-gallery',
  imports: [
    Card,
    CardBody,
    CardActions,
    ButtonRadioGroupComponent,
  ],
  templateUrl: './thumbnail-gallery.component.html',
  styleUrls: ['./thumbnail-gallery.component.scss'],
})
export class ThumbnailGalleryComponent implements AfterViewInit {
  scroller = inject(ViewportScroller);

  options = [
    {
      value: 'Text',
      icon: 'text_ad',
    },
    {
      value: 'Image',
      icon: 'image',
    },
    {
      value: 'Blank',
      icon: 'check_box_outline_blank',
    },
  ];

  ngAfterViewInit(): void {
    setTimeout(() => {
      window.scrollTo({
        top: 0,
        left: 0,
        behavior: 'smooth',
      });
    }, 10000);
  }
  app = inject(AppContextProvider).app;

  cycleImageType(page: Page, $event: any) {
    $event.preventDefault();
    const types: PageType[] = ['Text', 'Image', 'Blank'];
    let idx = types.findIndex((type) => page.type === type);
    idx = (idx + 1) % types.length;
    page.type = types[idx];
  }

  setPageType(page: Page, $event: string) {
    page.type = $event as PageType;
  }
}
