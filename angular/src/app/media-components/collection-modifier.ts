import { Component, inject, ViewEncapsulation } from '@angular/core';
import { App } from '../app';

@Component({
  selector: 'collection-modifier',
  imports: [],
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
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class Series {
  app = inject(App);
}
