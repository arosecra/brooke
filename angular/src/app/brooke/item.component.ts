import { Component, Input, inject } from '@angular/core';
import { ItemNamePipe } from './brooke.pipe';
import { Item } from './brooke.model';
import { BrookeService } from './brooke.service';
import { NgClass } from '@angular/common';

@Component({
    selector: 'item',
    // templateUrl: './item.component.html',
    template: `
		<div class="block content-end border-4 border-black">
  <img
    class="h-fit max-h-96 max-w-60"
    [ngClass]="{
      'aspect-video': brookeService.currentCollection()?.openType === 'video',
      'aspect-auto': brookeService.currentCollection()?.openType === 'book'
    }"
    src="/rest/thumbnail/{{ brookeService.currentCollection()?.name }}/{{
      item.name
    }}"
  />

  <h3 class="mt-4 font-bold text-gray-900">
    {{
      item
        | itemName
          : brookeService.currentCollection()
          : brookeService.currentCategory()
          : true
    }}
  </h3>

  <span
    class="inline-flex -space-x-px overflow-hidden rounded-md border bg-white shadow-sm"
  >
    <button
      (click)="openItem()"
      class="inline-block px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 focus:relative"
    >
      Open
    </button>

    @if (brookeService.currentCollection()?.openType === 'book') {
    <button
      (click)="copyToBooxTablet()"
      class="inline-block px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 focus:relative"
    >
      Boox
    </button>
    <button
      (click)="copyToKindleScribe()"
      class="inline-block px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50 focus:relative"
    >
      Scribe
    </button>
    }
  </span>
</div>

		`,
    imports: [ItemNamePipe, NgClass]
})
export class ItemComponent {
	@Input("item") item: Item;

	public brookeService: BrookeService = inject(BrookeService)
	component: {};

	openItem() {
		this.brookeService.openItem(this.item);
	}

	copyToBooxTablet() {
		this.brookeService.copyToDevice(this.item, 'boox');
	}

	copyToKindleScribe() {
		this.brookeService.copyToDevice(this.item, 'scribe');
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
