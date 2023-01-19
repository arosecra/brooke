import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { first } from 'rxjs';
import { Category, Collection, Item } from '../brooke.model';
import { BrookeService } from '../brooke.service';

@Component({
	selector: 'app-item',
	templateUrl: './item.component.html',
})
export class ItemComponent {
	@Input("item") item: Item;
	@Input("collection") collection: Collection;
	@Input("category") category: Category;

	@Input("openType") openType: string;

	constructor(
		private router: Router,
		private brookeService: BrookeService,
	) {
	}

	openItem() {
		let queryParams: any = {
			collection: this.collection.name,
			category: this.category.name,
			item: this.item.name,
		}

		if (this.collection.openType === 'video' && this.item.series) {
			queryParams.series = this.item.name;
			queryParams.item = undefined;
			this.router.navigate(['/series'], { queryParams: queryParams });
		} else {
			if (this.collection.openType === 'book') {
				queryParams.leftPage = 0;
				queryParams.rightPage = 1;
			}

			this.router.navigate(['/cache'], { queryParams: queryParams });
		}
	}

	openItemDetails() {
		let queryParams: any = {
			collection: this.collection.name,
			category: this.category.name,
			item: this.item.name,
		}

		this.router.navigate(['/book-detail'], { queryParams: queryParams });
	}
}
