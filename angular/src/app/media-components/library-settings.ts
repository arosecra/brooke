import { Component, inject, OnInit, ViewEncapsulation } from '@angular/core';
import { App } from '../app';
import { Item } from '../model/item';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'library-settings',
  imports: [MatButtonModule],
  template: `
		<!--
			3 things we can do. - Change the Category an item is in
                          - Add a Category
													- Set a Category in an Item that doesn't have one
			
			Show all items / series
			Have a radio to only show uncategorized items
			For each item, have a drop down for the category
			Also have a button to add a new category
		-->
		<h3>Uncategorized Items</h3>
		@if(uncategorizedItems.length > 0) {
			@for(uncatItem of uncategorizedItems; track uncatItem.collectionName + uncatItem.name) {
				<div> {{ uncatItem.collectionName }} / {{ uncatItem.name }}</div>
			}
		} @else {
			<div>There are no uncategorized items</div>
		}
		<button mat-raised-button (click)="app.appState.showLibraryEditorManual.set(false)">Done</button>
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class LibrarySettings implements OnInit {
  app = inject(App);

	uncategorizedItems: Item[] = [];

	ngOnInit() {
		const library = this.app.resources.storedLibrary.value();
		const items = library?.items;
		const categories = library?.categories;

		if(items && categories) {
			this.uncategorizedItems = items.filter((item) => 
				!categories.some((category) => 
					category.items.some((itemRef) => itemRef.name === item.name)
				)
			);
		}

	}
}
