import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from '../brooke.service';

@Component({
  selector: 'book',
  templateUrl: './book.component.html'
})
export class BookComponent {
	public brookeService: BrookeService = inject(BrookeService)

}
