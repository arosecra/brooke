import { Component, inject, signal } from '@angular/core';
import { CommonModule, NgStyle } from '@angular/common';
import { BrookeService } from './brooke.service';

@Component({
    selector: 'book-options',
    template: `
		@if(brookeService.currentItem()) {
	<div>
		<button class="button primary black-border" (click)="onTOCClick(true)">Options</button>

		<div class="w3-modal" [ngStyle]="{'display': isOpen() ? 'block' : 'none' }">
			<div class="w3-modal-content">
				<div class="w3-container">
					<h2>Options </h2><button (click)="onTOCClick(false)" class="w3-button w3-display-topright">&times;</button>
					<div>
	
					</div>
				</div>
			</div>
		</div>
	
						<!-- <header class="modal-card-head">
								<p class="modal-card-title">Options</p>
								<button class="delete" aria-label="close" (click)="onTOCClick(false)"></button>
						</header>
						<section class="modal-card-body">
								<div class="field">
										<div class="control">
											<label class="checkbox">
												<input type="checkbox">
												Crop Overlay
											</label>
										</div>
									</div>
						</section>
						<footer class="modal-card-foot">
								<button class="button is-success" (click)="onTOCClick(false)">Save changes</button>
								<button class="button" (click)="onTOCClick(false)">Cancel</button>
						</footer> -->

	</div>


}
		`,
    imports: [NgStyle]
})
export class BookOptionsComponent {
	public brookeService: BrookeService = inject(BrookeService)

	isOpen = signal<Boolean>(false);

	onTOCClick(newValue: boolean) {
		this.isOpen.update(() => newValue)
	}

}
