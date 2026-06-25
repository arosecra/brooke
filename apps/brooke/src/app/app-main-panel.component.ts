import {
  Component,
  inject,
  ViewEncapsulation,
} from '@angular/core';
import { AppContextProvider } from '../providers/app-context-provider';
import { DBContextProvider } from '../providers/db-context-provider';
import { SettingsComponent } from '../settings/settings';
import { ButtonComponent } from '../shared';
import { ItemCardComponent } from './media/cards/item-card/item-card.component';
import { SeriesComponent } from './media/cards/series-card/series-card.component';
import { BookComponent } from './media/lectern/lectern.component';
import { ThumbnailGalleryComponent } from './media/thumbnail-gallery/thumbnail-gallery.component';

@Component({
  selector: 'AppMainPanel',
  imports: [
    SettingsComponent,
    ItemCardComponent,
    ButtonComponent,
    SeriesComponent,
    BookComponent,
    ThumbnailGalleryComponent,
  ],
  template: `
    @if (
      !app.storedLibrary.value() || !app.settings.value()
    ) {
      <div>Loading...</div>
    } @else if (app.storedLibrary.hasValue()) {
      @if (
        app.mainPanelMode() === 'SETTINGS' ||
        app.settingsRequired()
      ) {
        <settings />
      } @else if (app.mainPanelMode() === 'COLLECTIONS') {
        <div class="flex flex-gap">
          @for (
            collection of app.storedLibrary.value()
              .collections;
            track collection.name
          ) {
            <Btn
              type="filled"
              class="margin"
              [label]="collection.name"
              (click)="app.openCollection(collection)"
            ></Btn>
          }
        </div>
      } @else if (app.mainPanelMode() === 'CATEGORIES') {
        <div class="flex flex-gap">
          @for (
            category of app.location().collectionCategories;
            track category.name
          ) {
            <Btn
              type="filled"
              class="margin"
              [label]="category.name"
              (click)="app.openCategory(category)"
            ></Btn>
          }
        </div>
      } @else if (app.mainPanelMode() === 'ITEMS') {
        <div
          class="flex flex-justify-content-center flex-gap"
          [class.books]="
            app.location().collection?.openType === 'book'
          "
          [class.videos]="
            app.location().collection?.openType !== 'book'
          "
        >
          @for (
            cItem of app.location().completeItems;
            track cItem.itemRef.name
          ) {
            <item-card [completeItem]="cItem"></item-card>
          }
        </div>
      } @else if (app.mainPanelMode() === 'SERIES') {
        TODO
        <!-- @let seriesCollectionAndName = app.currentCollection()!.name + '_' + app.currentSeries()?.name;
      @let seriesItem = app.storedLibrary.value()!.itemsByCollectionAndName[seriesCollectionAndName];
            
      <series-card [seriesItemRef]="app.currentSeries()!" [seriesItem]="seriesItem"
        [thumbnail]="app.currentCategoryThumbnails()[seriesItem.name]"></series-card> -->
      } @else if (app.mainPanelMode() === 'BOOK') {
        @if (app.bookViewMode() === 'IMAGE') {
          <lectern />
        } @else if (app.bookViewMode() === 'THUMBNAIL') {
          <thumbnail-gallery />
        }
      }
    }
  `,
  encapsulation: ViewEncapsulation.None,
})
export class AppMainPanelComponent {
  app = inject(AppContextProvider).app;
  appDB = inject(DBContextProvider).appDB;
}
