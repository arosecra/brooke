import { Component, inject, input, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { AppComponent } from '../app';
import { Item } from '../model/item';
import { ItemRef } from '../model/item-ref';
import { ItemCardComponent } from './item-card';

@Component({
  selector: 'series-card',
  imports: [ItemCardComponent],
  template: `
		<div class="flex flex-column flex-align-content-center">
			@for (childItemRef of seriesItemRef().childItems; track childItemRef.name) {
				@let childItem = seriesItem().childItems[$index];
				<item-card [itemRef]="childItemRef" 
					[item]="childItem"
					[seriesItemRef]="seriesItemRef()"
					[seriesItem]="seriesItem()"
				></item-card>
			}

			<img [src]="imageUrl" />
		</div>
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class SeriesComponent implements OnInit, OnDestroy {
  app = inject(AppComponent);

	seriesItem = input.required<Item>();
	seriesItemRef = input.required<ItemRef>();

	imageUrl: string;

	ngOnInit(): void {
		const item = this.seriesItem();
		if(item?.thumbnail) {
			this.imageUrl = URL.createObjectURL(item.thumbnail);
		}
	}
	ngOnDestroy(): void {
		if(this.imageUrl) URL.revokeObjectURL(this.imageUrl);
	}
}
