import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from '../brooke.service';

@Component({
  selector: 'panel',
  templateUrl: './panel.component.html'
})
export class PanelComponent {
	public brookeService: BrookeService = inject(BrookeService)

}
