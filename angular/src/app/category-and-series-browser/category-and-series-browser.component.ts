import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from '../brooke.service';

@Component({
  selector: 'category-and-series-browser',
  templateUrl: './category-and-series-browser.component.html'
})
export class CategoryAndSeriesBrowserComponent {
	public brookeService: BrookeService = inject(BrookeService)
}
