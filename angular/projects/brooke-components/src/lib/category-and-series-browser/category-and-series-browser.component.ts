import { Component, inject } from '@angular/core';
import { NgClass, NgStyle } from '@angular/common';
import { BrookeService } from 'brooke-state';
import { ItemComponent } from '../item/item.component';
import { JobDetailsComponent } from '../job-details/job-details.component';

@Component({
    selector: 'category-and-series-browser',
    templateUrl: './category-and-series-browser.component.html',
    standalone: true,
    imports: [NgStyle, NgClass, JobDetailsComponent, ItemComponent]
})
export class CategoryAndSeriesBrowserComponent {
	public brookeService: BrookeService = inject(BrookeService)
}
