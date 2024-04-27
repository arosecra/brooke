import { Component, HostListener, inject } from '@angular/core';
import { CommonModule, NgIf, NgFor, NgClass } from '@angular/common';
import { BrookeService } from '../brooke.service';
import { IconComponent } from "../icon/icon.component";

@Component({
    selector: 'book-page-turner',
    templateUrl: './book-page-turner.component.html',
    standalone: true,
    imports: [NgClass, IconComponent]
})
export class BookPageTurnerComponent {
	public brookeService: BrookeService = inject(BrookeService)

	
	@HostListener('document:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
		if(event.key === 'ArrowRight') {
			this.goToPage(this.brookeService.currentLeftPage(), 2)
		} else if(event.key === 'ArrowLeft') {
			this.goToPage(this.brookeService.currentLeftPage(), -2)
		}
  }

	goToPage(pageNo: string|undefined, offset: number) {
		let newPageNo = (Number(pageNo ?? 0)) + offset;
		this.brookeService.currentLeftPage.update(()=>''+newPageNo);
		this.brookeService.currentRightPage.update(()=>''+(newPageNo+1));
	}
}
