import { Component, inject, ViewEncapsulation } from '@angular/core';
import { AppComponent } from '../app';
import { ItemCardComponent } from './item-card';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { SeriesComponent } from './series';

@Component({
  selector: 'collection-browser',
  imports: [ItemCardComponent, MatButtonModule, MatChipsModule, MatIconModule, MatButtonModule, SeriesComponent],
  template: `
		@let appState = app.appState();
		@let resources = app.resources();
    @if (appState && resources && resources.storedLibrary.hasValue()) {
      @let library = resources.storedLibrary.value();
      @let currentCollection = appState.currentCollection();
      @let currentCategory = appState.currentCategory();
      @let currentSeries = appState.currentSeries();

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

      @if (currentCollection && currentCategory && !currentSeries) {
        <div class="flex" 
					[class.books]="currentCollection.openType === 'book'" 
				  [class.videos]="currentCollection.openType !== 'book'">
          @for (itemRef of currentCategory.items; track itemRef.name) {
						@let itemCollectionAndName = currentCollection.name + '_' + itemRef.name;
            <item-card [itemRef]="itemRef" [item]="library.itemsByCollectionAndName[itemCollectionAndName]"></item-card>
          }
        </div>
      }

			@if(currentCollection && currentSeries) {
				@let seriesCollectionAndName = currentCollection!.name + '_' + app.appState().currentSeries()?.name;
				@let seriesItem = library!.itemsByCollectionAndName[seriesCollectionAndName];
				<series-card [seriesItemRef]="currentSeries" [seriesItem]="seriesItem"></series-card>
			}
		}
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class CollectionBrowserComponent {
  app = inject(AppComponent);
}
