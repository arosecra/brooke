import { Component, inject, computed } from '@angular/core';
import { NgStyle, NgClass } from '@angular/common';
import { BrookeService } from './brooke.service';
import { Collection, Category } from './brooke.model';
import { IconComponent } from "./icon.component";

@Component({
    selector: 'collection-menu',
		template: `
<div class="h-32 rounded-lg bg-gray-200">
  <div class="flex h-screen flex-row justify-between border-e bg-white">
    <div class="px-4 py-6">
      <ul class="mt-6 space-y-1">
        @for (collection of brookeService.collections(); track collection.name)
        {
        <li>
          <details class="group [&_summary::-webkit-details-marker]:hidden">
            <summary
              class="flex cursor-pointer items-center justify-between rounded-lg px-4 py-2 text-gray-500 hover:bg-gray-100 hover:text-gray-700"
            >
              <span class="text-sm font-medium">{{
                collection.name.replaceAll("_", " ")
              }}</span>

              <span
                class="shrink-0 transition duration-300 group-open:-rotate-180"
              >
                <icon name="greater-than"></icon>
              </span>
            </summary>

            <ul class="mt-2 space-y-1 px-4">
              @for (category of collection?.categories; track category.name) {
              <li>
                <a
                  href="#"
                  (click)="onCategoryButtonClick(collection, category)"
                  class="block rounded-lg px-4 py-2 text-sm font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700"
                >
                  {{ category.name.replaceAll("_", " ") }}
                </a>
              </li>
              }
            </ul>
          </details>
        </li>
        }
      </ul>
    </div>
  </div>
</div>		
		`,
    standalone: true,
    imports: [NgStyle, NgClass, IconComponent]
})
export class CollectionMenuComponent {
	public brookeService: BrookeService = inject(BrookeService)

	onCategoryButtonClick(collection: Collection, category: Category) {
		if(this.brookeService.currentCategory()?.name === category.name) {
			this.brookeService.currentCategory.update(()=>undefined)
		} else {
			this.brookeService.currentCategory.update(() => category)
		}
		this.brookeService.currentCollection.update(() => collection);
		this.brookeService.currentSeries.update(()=>undefined)
		this.brookeService.currentItem.update(()=>undefined)
	}
}
