import { Component, computed, inject, signal } from '@angular/core';
import { AppComponent } from './app.component';
import { BookDetails } from './model/book-details';
import { Category } from './model/category';
import { Collection } from './model/collection';
import { Item } from './model/item';
import { ItemRef } from './model/item-ref';
import { Thumbnail } from './model/thumbnail';


@Component({
  selector: 'app-state',
  imports: [],
  template: ``,
  styles: ``,

  providers: [],
})
export class AppStateComponent {
  private app = inject(AppComponent);

  currentCollection = signal<Collection | undefined>(undefined);
  currentCategory = signal<Category | undefined>(undefined);
	currentCategoryThumbnails = signal<Record<string, Thumbnail>>({});
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
