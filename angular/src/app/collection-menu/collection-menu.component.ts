import { Component, inject, computed } from '@angular/core';
import { NgStyle, NgClass } from '@angular/common';
import { BrookeService } from '../brooke.service';
import { Collection, Category } from '../brooke.model';
import { IconComponent } from "../icon/icon.component";

@Component({
    selector: 'collection-menu',
    templateUrl: './collection-menu.component.html',
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
