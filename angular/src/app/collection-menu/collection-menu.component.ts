import { Component, inject, computed } from '@angular/core';
import { BrookeService } from '../brooke.service';
import { Category, Collection } from '../brooke.model';

@Component({
  selector: 'collection-menu',
  templateUrl: './collection-menu.component.html'
})
export class CollectionMenuComponent {
	public brookeService: BrookeService = inject(BrookeService)

	onCollectionButtonClick(collection: Collection) {
		if(this.brookeService.currentCollection()?.name === collection.name) {
			this.brookeService.currentCollection.update(()=>undefined)
		} else {
			this.brookeService.currentCollection.update(() => collection);
		}
		this.brookeService.currentCategory.update(()=>undefined)
		this.brookeService.currentSeries.update(()=>undefined)
		this.brookeService.currentItem.update(()=>undefined)
	}

	onCategoryButtonClick(category: Category) {
		if(this.brookeService.currentCategory()?.name === category.name) {
			this.brookeService.currentCategory.update(()=>undefined)
		} else {
			this.brookeService.currentCategory.update(() => category)
		}
		this.brookeService.currentSeries.update(()=>undefined)
		this.brookeService.currentItem.update(()=>undefined)
	}
}
