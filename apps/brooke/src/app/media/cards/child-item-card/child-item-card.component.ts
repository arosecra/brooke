import { Component, inject, input, OnDestroy, OnInit } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { ActionComponent } from '../../../actions/action.component';
import { AppComponent } from '../../../app.component';
import { ChildItemRef } from '../../../model/child-item-ref';
import { Item } from '../../../model/item';
import { ItemRef } from '../../../model/item-ref';
import { Thumbnail } from '../../../model/thumbnail';
import { ChildItem } from '../../../model/child-item';

@Component({
  selector: 'child-item-card',
  imports: [MatButtonModule, MatIconModule, MatCardModule, ActionComponent, MatBadgeModule],
  templateUrl: './child-item-card.component.html',
  styleUrls: ['./child-item-card.component.scss'],

})
export class ChildItemCardComponent {
  app = inject(AppComponent);
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
		if(seriesItem && seriesItemRef) {
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
