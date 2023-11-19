import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from 'brooke-state';

@Component({
    selector: 'breadcrumb',
    templateUrl: './breadcrumb.component.html',
    standalone: true
})
export class BreadcrumbComponent {
	public brookeService: BrookeService = inject(BrookeService)

	openHome() {
		this.brookeService.currentCollection.set(undefined);
		this.brookeService.currentCategory.set(undefined);
		this.brookeService.currentSeries.set(undefined);
		this.brookeService.currentItem.set(undefined);
	}

	openCollection() {
		this.brookeService.currentCategory.set(undefined);
		this.brookeService.currentSeries.set(undefined);
		this.brookeService.currentItem.set(undefined);
	}

	openCategory() {
		this.brookeService.currentSeries.set(undefined);
		this.brookeService.currentItem.set(undefined);
	}
}
