import { Component, inject, input } from '@angular/core';
import { AppContextProvider } from '../../../../providers/app-context-provider';
import {
  ButtonComponent,
  ChildItem,
  ChildItemRef,
  Item,
  ItemRef,
  Thumbnail,
} from '../../../../shared';
import { Card } from '../card/card';
import { CardActions } from '../card/card-actions';
import { CardHeader } from '../card/card-header';

@Component({
  selector: 'child-item-card',
  imports: [ButtonComponent, CardActions, CardHeader, Card],
  templateUrl: './child-item-card.component.html',
  styleUrls: ['./child-item-card.component.scss'],
})
export class ChildItemCardComponent {
  app = inject(AppContextProvider).app;
  item = input.required<ChildItem>();
  itemRef = input.required<ChildItemRef>();
  seriesItemRef = input.required<ItemRef>();
  seriesItem = input.required<Item>();
  thumbnail = input<Thumbnail>();

  imageUrl: string;

  downloadForOffline() {
    return this.app.cacheItem(this.item());
  }

  openItem() {
    const seriesItemRef = this.seriesItemRef();
    const seriesItem = this.seriesItem();
    if (seriesItem && seriesItemRef) {
      return this.app.openSeriesItem(
        seriesItemRef,
        seriesItem,
      );
    } else {
      return this.app.openItem(this.item());
    }
  }

  openItemDetails() {
    return Promise.resolve(true);
  }
}
