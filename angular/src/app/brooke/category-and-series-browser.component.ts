import { Component, inject } from '@angular/core';
import { NgClass, NgStyle } from '@angular/common';
import { ItemComponent } from './item.component';
import { JobDetailsComponent } from './job-details.component';
import { BrookeService } from './brooke.service';

@Component({
    selector: 'category-and-series-browser',
		template: `

@if(brookeService.currentJob()) {
	<job-details></job-details>
} @else if(brookeService.currentCategory()) {	
	<div class="grid grid-cols-7 gap-4 lg:gap-8">

		<div class="col-span-6 grid grid-cols-5 gap-4">
			@for (item of brookeService.currentCategory()?.items; track item.name) {
				<div class="">
					<item [item]="item" style="display: contents;"></item>
				</div>
			}
		</div>
	</div>
}
		
		`,
    standalone: true,
    imports: [NgStyle, NgClass, JobDetailsComponent, ItemComponent]
})
export class CategoryAndSeriesBrowserComponent {
	public brookeService: BrookeService = inject(BrookeService)
}
