import { Component, inject, input } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { AppContextProvider } from '../../../../providers/app-context-provider';
import { ChildItem, ChildItemRef, Item, ItemRef, Thumbnail } from '../../../../shared';
import { ActionComponent } from '../../../actions/action.component';

@Component({
  selector: 'child-item-card',
  imports: [MatButtonModule, MatIconModule, MatCardModule, ActionComponent, MatBadgeModule],
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
      return this.app.openSeriesItem(seriesItemRef, seriesItem);
    } else {
      return this.app.openItem(this.item());
    }
  }

  openItemThumbnails() {
    return this.app.openItemThumbnails(this.item());
  }

  openItemMarkdown() {
    return this.app.openItemMarkdown(this.item());
  }

  openItemDetails() {
    return Promise.resolve(true);
  }
}
