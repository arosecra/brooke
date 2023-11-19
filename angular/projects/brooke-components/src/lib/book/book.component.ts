import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from 'brooke-state';

@Component({
    selector: 'book',
    templateUrl: './book.component.html',
    standalone: true
})
export class BookComponent {
	public brookeService: BrookeService = inject(BrookeService)

}
