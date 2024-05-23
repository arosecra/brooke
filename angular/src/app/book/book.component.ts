import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from '../brooke.service';
import { Page } from '../brooke.model';

@Component({
    selector: 'book',
    template: `
<div class="brooke-pages-grid"
  style="padding: 0em 0em"
>
  <div >
    <img
      style="width: 100%"
      src="/rest/page/{{ brookeService.currentCollection()?.name }}/{{
        brookeService.currentItem()?.name
      }}/{{ brookeService.currentLeftPage() }}"
    />
  </div>
  <div >
    <img
			style="width: 100%"
      src="/rest/page/{{ brookeService.currentCollection()?.name }}/{{
        brookeService.currentItem()?.name
      }}/{{ brookeService.currentRightPage() }}"
    />
  </div>
</div>
		
		`,
    standalone: true
})
export class BookComponent {
	public brookeService: BrookeService = inject(BrookeService);

	



}
