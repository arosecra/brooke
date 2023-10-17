import { Component, inject } from '@angular/core';
import { BrookeService } from '../brooke.service';

@Component({
  selector: 'top-bar',
  templateUrl: './top-bar.component.html'
})
export class TopBarComponent {
	public brookeService: BrookeService = inject(BrookeService)

}
