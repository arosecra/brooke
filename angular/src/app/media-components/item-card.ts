import { Component, inject, input, OnInit, ViewEncapsulation } from '@angular/core';
import { App } from '../app';
import { NgClass } from '@angular/common';
import { ItemNamePipe } from './itemName.pipe';
import { ItemRef } from '../model/item-ref';
import { Item } from '../model/item';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { PromiseButton } from '../mini-components/pbutton';
import { TestDir } from '../mini-components/test-dir';

@Component({
  selector: 'item-card',
  imports: [ItemNamePipe, MatButtonModule, MatIconModule, MatCardModule, PromiseButton, TestDir],
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
        <img mat-card-image [src]="item().thumbnail" />
        <mat-card-actions>
          <div>
            <button matMiniFab (click)="openItem()" name="open">
              <mat-icon fontSet="material-symbols-outlined">file_open</mat-icon>
            </button>
            <button matMiniFab (click)="downloadForOffline()" miniAsync>
              <mat-icon fontSet="material-symbols-outlined">download_for_offline</mat-icon>
            </button>
            <button matMiniFab (click)="openItemDetails()" title="details">
              <mat-icon fontSet="material-symbols-outlined">notes</mat-icon>
            </button>
            @if (appState.currentCollection()?.openType === 'book') {
              <button matMiniFab (click)="openItemThumbnails()" title="Thumbnails" >
                <mat-icon fontSet="material-symbols-outlined">dataset</mat-icon>
              </button>
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

  downloadForOffline() {}

  openItem() {
    this.app.openItem(this.itemRef(), this.item());
  }

  openItemThumbnails() {
    return this.app.openItemThumbnails(this.itemRef(), this.item());
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
  }
}
