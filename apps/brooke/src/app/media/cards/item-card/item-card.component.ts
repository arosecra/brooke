import { Component, inject, input, OnDestroy, OnInit } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { ActionComponent } from '../../../actions/action.component';
import { AppComponent } from '../../../app.component';
import { Item } from '../../../model/item';
import { ItemRef } from '../../../model/item-ref';
import { Thumbnail } from '../../../model/thumbnail';

@Component({
  selector: 'item-card',
  imports: [MatButtonModule, MatIconModule, MatCardModule, ActionComponent, MatBadgeModule],
  templateUrl: './item-card.component.html',
  styleUrls: ['./item-card.component.scss'],

})
export class ItemCardComponent implements OnInit, OnDestroy {
  app = inject(AppComponent);
  itemRef = input.required<ItemRef>();
  item = input.required<Item>();
	thumbnail = input<Thumbnail>();

	imageUrl: string;

	ngOnInit(): void {
		const item = this.thumbnail();
		if(item) {
			this.imageUrl = URL.createObjectURL(item.thumbnail);
		}
	}
	ngOnDestroy(): void {
		if(this.imageUrl) URL.revokeObjectURL(this.imageUrl);
	}

  downloadForOffline() {
		return this.app.cacheItem(this.item());
	}

  openItem() {
    return this.app.openItem(this.item());
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
