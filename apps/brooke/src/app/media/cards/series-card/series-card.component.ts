import { Component, inject, input, OnDestroy, OnInit } from '@angular/core';
import { AppContextProvider } from '../../../../providers/app-context-provider';
import { Item, ItemRef, Thumbnail } from '../../../../shared';
import { ChildItemCardComponent } from '../child-item-card/child-item-card.component';

@Component({
  selector: 'series-card',
  imports: [ChildItemCardComponent],
  template: `
    <div class="flex flex-column flex-align-content-center">
      @for (childItemRef of seriesItemRef().childItems; track childItemRef.name) {
        @let childItem = seriesItem().childItems[$index];
        <child-item-card
          [itemRef]="childItemRef"
          [itemRef]="childItemRef"
          [item]="childItem"
          [seriesItemRef]="seriesItemRef()"
          [seriesItem]="seriesItem()"
        ></child-item-card>
      }

      <img [src]="imageUrl" />
    </div>
  `,
  styles: ``,
})
export class SeriesComponent implements OnInit, OnDestroy {
  app = inject(AppContextProvider).app;

  seriesItem = input.required<Item>();
  seriesItemRef = input.required<ItemRef>();
  thumbnail = input<Thumbnail>();

  imageUrl: string;

  ngOnInit(): void {
    const thumbnail = this.thumbnail();
    if (thumbnail?.thumbnail) {
      this.imageUrl = URL.createObjectURL(thumbnail.thumbnail);
    }
  }
  ngOnDestroy(): void {
    if (this.imageUrl) URL.revokeObjectURL(this.imageUrl);
  }
}
