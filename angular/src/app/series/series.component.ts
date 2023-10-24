import { Component, inject } from '@angular/core';
import { CommonModule, NgFor } from '@angular/common';
import { BrookeService } from '../brooke.service';
import { Item } from '../brooke.model';

@Component({
    selector: 'series',
    templateUrl: './series.component.html',
    standalone: true,
    imports: [NgFor]
})
export class SeriesComponent {
	public brookeService: BrookeService = inject(BrookeService)

	openItem(childItem: Item) {
		this.brookeService.openItem(childItem);
	}
}
