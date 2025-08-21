import { Component, inject, signal } from '@angular/core';
import { CommonModule, NgStyle } from '@angular/common';
import { BrookeService } from './brooke.service';

@Component({
    selector: 'book-toc',
    template: `
<div>
  <button class="button primary black-border" (click)="onTOCClick(true)">
    TOC
  </button>

  <div class="w3-modal" [ngStyle]="{ display: isOpen() ? 'block' : 'none' }">
    <div class="w3-modal-content">
      <div class="w3-container">
        <h2>Table of Contents</h2>
        <button
          (click)="onTOCClick(false)"
          class="w3-button w3-display-topright"
        >
          &times;
        </button>
        <div>
          <table class="table">
            <thead>
              <tr>
                <th></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              @for (tocEntry of brookeService.currentBookDetails()?.tocEntries;
              track tocEntry.name) {
              <tr (click)="goToPage(tocEntry.pageNumber)">
                <td>{{ tocEntry.pageNumber }}</td>
                <td>{{ tocEntry.name }}</td>
              </tr>
              }
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
		
		`,
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
