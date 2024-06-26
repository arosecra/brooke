import { Component, inject } from '@angular/core';
import { CommonModule, NgStyle } from '@angular/common';
import { CategoryAndSeriesBrowserComponent } from './category-and-series-browser.component';
import { SeriesComponent } from './series.component';
import { BookComponent } from './book.component';
import { CollectionMenuComponent } from './collection-menu.component';
import { BrookeService } from './brooke.service';
import { JobQueueComponent } from './job-queue.component';

@Component({
    selector: 'panel',
		template: `
			@if(brookeService.currentItem() && brookeService.currentCollection()?.openType === 'book') {
				<book style="display: contents;"></book>
			} @else if(brookeService.currentSeries()) {
				<series style="display: contents;"></series>
			} @else if(brookeService.jobQueue()) {
				<job-queue style="display: contents;"></job-queue>
			} @else {
				<category-and-series-browser style="display: contents;"></category-and-series-browser>
			}
		`,
    standalone: true,
    imports: [CollectionMenuComponent, NgStyle, BookComponent, SeriesComponent, CategoryAndSeriesBrowserComponent, JobQueueComponent]
})
export class PanelComponent {
	public brookeService: BrookeService = inject(BrookeService)

}
