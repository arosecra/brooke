import { Component, computed, inject, signal, ViewEncapsulation } from '@angular/core';
import { App } from './app';
import { BookDetails } from './model/book-details';
import { Category } from './model/category';
import { Collection } from './model/collection';
import { Item } from './model/item';
import { ItemRef } from './model/item-ref';


@Component({
  selector: 'app-state',
  imports: [],
  template: ``,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
  providers: [],
})
export class AppState {
  app = inject(App);

  currentCollection = signal<Collection | undefined>(undefined);
  currentCategory = signal<Category | undefined>(undefined);
  currentSeries = signal<ItemRef | undefined>(undefined);
  currentItem = signal<Item | undefined>(undefined);
  currentPageSet = signal<number>(0);
  currentBookDetails = signal<BookDetails | undefined>(undefined);

  showSettingsManual = signal<boolean>(false);
  showLibraryEditorManual = signal<boolean>(false);

  showSettingsRequired = computed(() => {
    const library = this.app.resources()?.storedLibrary.value();

    const hasCollections = library?.collections && library?.collections?.length > 0;
    const hasCollectionMissingPermissions = !!library?.collections.some((val: any) => {
      return !val.hasPermission;
    });

    return (
      (!!library?.cacheDirectory && !library?.cacheDirectory?.hasPermission) ||
      !hasCollections ||
      hasCollectionMissingPermissions
    );
  });
}
