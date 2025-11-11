import { Component, inject, input, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../app';
import { ActionComponent } from '../mini-components/action';
import { Item } from '../model/item';
import { ItemRef } from '../model/item-ref';
import { ItemNamePipe } from './itemName.pipe';
import { MatBadgeModule } from '@angular/material/badge';

@Component({
  selector: 'item-card',
  imports: [ItemNamePipe, MatButtonModule, MatIconModule, MatCardModule, ActionComponent, MatBadgeModule],
  template: `
    @let appState = app.appState();
    @let itm = item();
		@let mainActionItem = appState.currentCollection()?.openType === 'book' ? 'file_open' : 'play_arrow';
    @if (appState && itm) {
      <mat-card [class.childitem]="seriesItemRef()">
        <mat-card-header>
          <mat-card-title
            >{{
              item() | itemName: appState.currentCollection() : appState.currentCategory() : true
            }}
          </mat-card-title>
        </mat-card-header>
				@if(imageUrl) {
					<action mat-card-image [img]="imageUrl" [m]="openItem" [o]="this">{{mainActionItem}}</action>
				}
        <mat-card-actions>
          <div>
						<action [m]="openItem" [o]="this">{{mainActionItem}}</action>
						<action [matBadge]="item().childItems?.length" matBadgeSize="large" [matBadgeHidden]="!item().childItems?.length" 
							 [m]="downloadForOffline" [o]="this">download_for_offline</action>
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
export class ItemCardComponent implements OnInit, OnDestroy {
  app = inject(AppComponent);
  itemRef = input.required<ItemRef>();
  item = input.required<Item>();
	seriesItemRef = input<ItemRef>();
	seriesItem = input<Item>();

	imageUrl: string;

	ngOnInit(): void {
		const item = this.item();
		if(item?.thumbnail) {
			this.imageUrl = URL.createObjectURL(item.thumbnail);
		}
	}
	ngOnDestroy(): void {
		if(this.imageUrl) URL.revokeObjectURL(this.imageUrl);
	}


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
