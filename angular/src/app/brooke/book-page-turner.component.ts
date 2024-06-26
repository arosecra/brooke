import { Component, HostListener, inject } from '@angular/core';
import { CommonModule, NgIf, NgFor, NgClass } from '@angular/common';
import { BrookeService } from './brooke.service';
import { IconComponent } from "./icon.component";

@Component({
    selector: 'book-page-turner',
		template: `
<div class="inline-flex items-center justify-center gap-3">
  <a
    href="#"
    (click)="goToPage(brookeService.currentLeftPage(), -2)"
    class="inline-flex size-8 items-center justify-center rounded border border-gray-100 bg-white text-gray-900 rtl:rotate-180"
  >
    <span class="sr-only">Next Page</span>
    <icon name="less-than"></icon>
  </a>

  <p class="text-xs text-gray-900">
    {{ brookeService.currentLeftPage() }}
    <span class="mx-0.25">/</span>
    {{ brookeService.currentBookDetails()?.numberOfPages }}
  </p>

  <a
    href="#"
    (click)="goToPage(brookeService.currentLeftPage(), 2)"
    class="inline-flex size-8 items-center justify-center rounded border border-gray-100 bg-white text-gray-900 rtl:rotate-180"
  >
    <span class="sr-only">Next Page</span>
    <icon name="greater-than"></icon>
  </a>
</div>
		
		`,
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
