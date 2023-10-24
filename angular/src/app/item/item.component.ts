import { Component, Input, inject } from '@angular/core';
import { Item } from '../brooke.model';
import { BrookeService } from '../brooke.service';
import { ItemNamePipe } from '../brooke.pipe';
import { NgIf } from '@angular/common';

@Component({
    selector: 'item',
    templateUrl: './item.component.html',
    standalone: true,
    imports: [NgIf, ItemNamePipe]
})
export class ItemComponent {
	@Input("item") item: Item;

	public brookeService: BrookeService = inject(BrookeService)

	openItem() {
		this.brookeService.openItem(this.item);
	}

	copyToTablet() {
		this.brookeService.copyToTablet(this.item);
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
