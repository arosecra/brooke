import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from '../brooke.service';
import { Item } from '../brooke.model';

@Component({
    selector: 'series',
    // templateUrl: './series.component.html',
		template: `
		<div class="flex flex-row">

			<div class="">
					<ul class="">
						@for (childItem of brookeService.currentSeries()?.childItems; track childItem.name) {
							<li>
								<a class="button menu-label" (click)="openItem(childItem)">{{childItem.name.replaceAll('_', ' ')}}</a>
							</li>
						}
					</ul>
			</div>

			<div class="">
					<img style="width: 100%" src="/rest/large-thumbnail/{{brookeService.currentCollection()?.name}}/{{brookeService.currentSeries()?.name}}">
			</div>

		</div>
		`,
    standalone: true,
    imports: []
})
export class SeriesComponent {
	public brookeService: BrookeService = inject(BrookeService)

	openItem(childItem: Item) {
		this.brookeService.openItem(childItem);
	}
}
