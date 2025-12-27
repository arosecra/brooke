import { Component, inject, OnInit } from '@angular/core';
import { AppComponent } from '../app.component';
import { Item } from '../model/item';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'library-settings',
  imports: [MatButtonModule],
  template: `
    @let appState = app.appState();
    <!--
			3 things we can do. - Change the Category an item is in
                          - Add a Category
													- Set a Category in an Item that doesn't have one
			
			Show all items / series
			Have a radio to only show uncategorized items
			For each item, have a drop down for the category
			Also have a button to add a new category
		-->
    @if (appState) {
      <h3>Uncategorized Items</h3>
      @if (uncategorizedItems.length > 0) {
        @for (uncatItem of uncategorizedItems; track uncatItem.collectionName + uncatItem.name) {
          <div>{{ uncatItem.collectionName }} / {{ uncatItem.name }}</div>
        }
      } @else {
        <div>There are no uncategorized items</div>
      }
      <button mat-raised-button (click)="appState.showLibraryEditorManual.set(false)">Done</button>
    }
  `,
  styles: ``,
})
export class LibrarySettingsComponent implements OnInit {
  app = inject(AppComponent);

  uncategorizedItems: Item[] = [];

  ngOnInit() {
    const library = this.app.resources()?.storedLibrary.value();
    const items = library?.items;
    const categories = library?.categories;

    if (items && categories) {
      this.uncategorizedItems = items.filter(
        (item) => !categories.some((category) => category.items.some((itemRef) => itemRef.name === item.name)),
      );
    }
  }
}
