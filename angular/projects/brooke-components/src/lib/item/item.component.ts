import { Component, Input, inject } from '@angular/core';
import { Item } from 'brooke-domain';
import { BrookeService } from 'brooke-state';
import { ItemNamePipe } from '../brooke.pipe';

@Component({
    selector: 'item',
    templateUrl: './item.component.html',
    standalone: true,
    imports: [ItemNamePipe]
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
