import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from '../brooke.service';

@Component({
    selector: 'modify-collection-button',
    templateUrl: './modify-collection-button.component.html',
    standalone: true
})
export class ModifyCollectionButtonComponent {
	public brookeService: BrookeService = inject(BrookeService)


	onEditCollectionClick() {

	}
}
