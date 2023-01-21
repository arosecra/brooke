import { Component } from '@angular/core';
import { map, Observable } from 'rxjs';
import { MissingItem } from '../brooke.model';
import { BrookeService } from '../brooke.service';

interface MissingItemsVM {
	missingItems: MissingItem[];
	missingItemCnt: number;
	missingShelfItemCnt: number;
}

@Component({
  selector: 'app-missing-items',
  templateUrl: './missing-items.component.html'
})
export class MissingItemsComponent {
	vm$: Observable<MissingItemsVM>;

	constructor(private brookeService: BrookeService){
	}

	ngOnInit() {
		this.vm$ = this.brookeService.getMissingItems().pipe( 
			map((missingItems) => {
				let missingItemCnt: number = 0;
				let missingShelfItemCnt: number = 0;

				missingItems.forEach((missingItem) => {
					if(missingItem.itemMissing)
						missingItemCnt++;
					else 
						missingShelfItemCnt++;
				})

				return {
					missingItems: missingItems,
					missingItemCnt: missingItemCnt,
					missingShelfItemCnt: missingShelfItemCnt
				};
			})
		)
	}
}
