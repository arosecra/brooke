import { Component, inject, signal } from '@angular/core';
import { CommonModule, NgStyle } from '@angular/common';
import { BrookeService } from '../brooke.service';

@Component({
    selector: 'book-options',
    templateUrl: './book-options.component.html',
    standalone: true,
    imports: [NgStyle]
})
export class BookOptionsComponent {
	public brookeService: BrookeService = inject(BrookeService)

	isOpen = signal<Boolean>(false);

	onTOCClick(newValue: boolean) {
		this.isOpen.update(() => newValue)
	}

}
