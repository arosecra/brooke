import { Component, inject, input, OnInit, ViewEncapsulation } from '@angular/core';
import { App } from '../app';
import { NgClass } from '@angular/common';
import { ItemNamePipe } from './itemName.pipe';
import { ItemRef } from '../model/item-ref';
import { Item } from '../model/item';
import { MatAnchor } from "@angular/material/button";

@Component({
  selector: 'item-card',
  imports: [NgClass, ItemNamePipe, MatAnchor],
  template: `
		@let appState = app.appState();
		@let itm = item();
		@if(appState && itm) {
    <img
      [ngClass]="{
        'aspect-video': appState.currentCollection()?.openType === 'video',
        'aspect-auto': appState.currentCollection()?.openType === 'book',
      }"
      [src]="item().thumbnail"
    />
    <div>
      <p>
        <strong>
          {{
            item()
              | itemName: appState.currentCollection() : appState.currentCategory() : true
          }}
        </strong>
      </p>
      <button matButton="tonal" (click)="openItem()">Open</button>
      <!-- @if (app.appState.currentCollection()?.openType === 'book') {
        <button (click)="copyToBooxTablet()">Boox</button>
        <button (click)="copyToKindleScribe()">Scribe</button>
      } -->
    </div>
		}
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class ItemCard {
  app = inject(App);
  itemRef = input.required<ItemRef>();
  item = input.required<Item>();

  openItem() {
    this.app.openItem(this.itemRef(), this.item());
  }

  copyToBooxTablet() {
    // this.app.copyToDevice(this.item, 'boox');
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
  }
}
