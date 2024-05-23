import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from './brooke.service';
import { IconComponent } from "./icon.component";

@Component({
    selector: 'breadcrumb',
		template: `
<nav aria-label="Breadcrumb">
	<ol class="flex items-center gap-1 text-sm text-gray-600"> 
		<li>
			<a href="#" class="block transition hover:text-gray-700" (click)="openHome()">
				<span class="sr-only"> Home </span>
				<icon name="home"></icon>
			</a>
		</li>

		@if(brookeService.currentCollection()) {
			<li class="rtl:rotate-180">
				<icon name="greater-than"></icon>
			</li>

			<li>
				<a href="#" class="block transition hover:text-gray-700" (click)="openCollection()"> {{brookeService.currentCollection()?.name}} </a>
			</li>
		}

		@if(brookeService.currentCategory()) {
			<li class="rtl:rotate-180">
				<icon name="greater-than"></icon>
			</li>

			<li>
				<a href="#" class="block transition hover:text-gray-700" (click)="openCategory()"> {{brookeService.currentCategory()?.name}} </a>
			</li>
		}
	</ol>
</nav>		
		`,
    standalone: true,
    imports: [IconComponent]
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
