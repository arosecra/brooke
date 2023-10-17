import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from '../brooke.service';

@Component({
  selector: 'book-page-turner',
  templateUrl: './book-page-turner.component.html'
})
export class BookPageTurnerComponent {
	public brookeService: BrookeService = inject(BrookeService)

	goToPage(pageNo: string|undefined, offset: number) {
		let newPageNo = (Number(pageNo ?? 0)) + offset;
		this.brookeService.currentLeftPage.update(()=>''+newPageNo);
		this.brookeService.currentRightPage.update(()=>''+(newPageNo+1));
	}
}
