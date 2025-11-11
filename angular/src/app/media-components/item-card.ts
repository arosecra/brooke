import { Component, inject, input, ViewEncapsulation } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { App } from '../app';
import { Action } from '../mini-components/action';
import { Item } from '../model/item';
import { ItemRef } from '../model/item-ref';
import { ItemNamePipe } from './itemName.pipe';

@Component({
  selector: 'item-card',
  imports: [ItemNamePipe, MatButtonModule, MatIconModule, MatCardModule, Action],
  template: `
    @let appState = app.appState();
    @let itm = item();
    @if (appState && itm) {
      <mat-card>
        <mat-card-header>
          <mat-card-title
            >{{
              item() | itemName: appState.currentCollection() : appState.currentCategory() : true
            }}
          </mat-card-title>
        </mat-card-header>
				<action mat-card-image [img]="item().thumbnail" [m]="openItem" [o]="this">file_open</action>
        <mat-card-actions>
          <div>
						<action [m]="openItem" [o]="this">file_open</action>
						<action [m]="downloadForOffline" [o]="this">download_for_offline</action>
						<action [m]="openItemDetails" [o]="this">notes</action>
							@if (appState.currentCollection()?.openType === 'book') {
								<action [m]="openItemThumbnails" [o]="this" title="Thumbnails">dataset</action>
								<action [m]="openItemMarkdown" [o]="this" title="Markdown">markdown</action>
							}
          </div>
        </mat-card-actions>
      </mat-card>
    }
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class ItemCard {
  app = inject(App);
  itemRef = input.required<ItemRef>();
  item = input.required<Item>();

  downloadForOffline() {
		return Promise.resolve(true);
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
