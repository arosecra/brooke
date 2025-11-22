import { Component, inject, input, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../../../app.component';
import { ActionComponent } from '../../../shared/action.component';
import { Item } from '../../../model/item';
import { ItemRef } from '../../../model/item-ref';
import { ItemNamePipe } from '../../../shared/item-name.pipe';
import { MatBadgeModule } from '@angular/material/badge';
import { Thumbnail } from '../../../model/thumbnail';

@Component({
  selector: 'item-card',
  imports: [ItemNamePipe, MatButtonModule, MatIconModule, MatCardModule, ActionComponent, MatBadgeModule],
  templateUrl: './item-card.component.html',
  styleUrls: ['./item-card.component.scss'],

})
export class ItemCardComponent implements OnInit, OnDestroy {
  app = inject(AppComponent);
  itemRef = input.required<ItemRef>();
  item = input.required<Item>();
	seriesItemRef = input<ItemRef>();
	seriesItem = input<Item>();
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
		return this.app.cacheItem(this.itemRef(), this.item());
	}

  openItem() {
    return this.app.openItem(this.itemRef(), this.item());
  }

  openItemThumbnails() {
    return this.app.openItemThumbnails(this.itemRef(), this.item());
  }

  openItemMarkdown() {
    return this.app.openItemMarkdown(this.itemRef(), this.item());
  }

  copyToBooxTablet() {
    // this.app.copyToDevice(this.item, 'boox');
    
		return new Promise((resolve, reject) => {
      setTimeout(() => {
        resolve('done');
      }, 30 * 1000);
    });
  }

  copyToKindleScribe() {
    // this.app.copyToDevice(this.item, 'scribe');
  }

  openItemDetails() {
    // let queryParams: any = {
    // 	collection: this.collection.name,
    // 	category: this.category.name,
    // 	item: this.item.name,
    // }
    // this.router.navigate(['/book-detail'], { queryParams: queryParams });
		return Promise.resolve(true);
  }
}
