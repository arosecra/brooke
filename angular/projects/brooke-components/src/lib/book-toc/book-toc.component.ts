import { Component, inject, signal } from '@angular/core';
import { CommonModule, NgStyle } from '@angular/common';
import { BrookeService } from 'brooke-state';

@Component({
    selector: 'book-toc',
    templateUrl: './book-toc.component.html',
    standalone: true,
    imports: [NgStyle]
})
export class BookTocComponent {
	public brookeService: BrookeService = inject(BrookeService)

	isOpen = signal<Boolean>(false);

	onTOCClick(newValue: boolean) {
		this.isOpen.update(() => newValue)
	}
	
  isTocPresent(): any {
    return false;
  }

  goToPage(page: number) {
    // this.router.navigate(['/book'], {
    //   queryParams: {
    //     collection: vm.collection,
    //     category: vm.category,
    //     item: vm.item,
    //     leftPage: page,
    //     rightPage: parseFloat(page) + 1,
    //   }
    // }
    // )
  }
}
