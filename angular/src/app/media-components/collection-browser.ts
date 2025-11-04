import { Component, inject, ViewEncapsulation } from '@angular/core';
import { App } from '../app';
import { ItemCard } from './item-card';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'collection-browser',
  imports: [ItemCard, MatButtonModule, MatChipsModule, MatIconModule, MatButtonModule],
  template: `
		@let appState = app.appState();
		@let resources = app.resources();
    @if (appState && resources && resources.storedLibrary.hasValue()) {
      @let library = resources.storedLibrary.value();
      @let currentCollection = appState.currentCollection();
      @let currentCategory = appState.currentCategory();

      @if (!currentCollection) {
        <h2>Collections</h2>
        <div class="flex">
          @for (collection of library.collections; track collection.name) {
            <button matButton="tonal" (click)="app.openCollection(collection)">
              {{ collection.name.replaceAll('_', ' ') }}
            </button>
          }
        </div>
      }

      @if (currentCollection && !currentCategory) {
        <h2>Categories</h2>
        <div class="flex">
          @for (category of library.categories; track $index) {
            @if (category.collectionName === currentCollection.name) {
              <button matButton="tonal" (click)="app.openCategory(category)">
                {{ category.name.replaceAll('_', ' ') }}
              </button>
            }
          }
        </div>
      }

      @if (currentCollection && currentCategory) {
        <div class="flex">
          @for (itemRef of currentCategory.items; track itemRef.name) {
						@let itemCollectionAndName = currentCollection.name + '_' + itemRef.name;
            <item-card [itemRef]="itemRef" [item]="library.itemsByCollectionAndName[itemCollectionAndName]"></item-card>
          }
        </div>
      }
    }
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class CollectionBrowser {
  app = inject(App);
}
