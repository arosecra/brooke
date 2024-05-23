import { Component, inject } from '@angular/core';
import { TopBarComponent } from './top-bar.component';
import { PanelComponent } from './panel.component';
import { BrookeService } from './brooke.service';
import { CollectionMenuComponent } from "./collection-menu.component";

@Component({
    selector: 'app-root',
    // templateUrl: './app.component.html',
		template: `
		<div class="flex flex-col">
			<top-bar style="display: contents"></top-bar>
			<div class="brooke-main flex flex-row gap-4">
				@if(this.brookeService.widgets.asideMenuExpanded()) {
					<collection-menu style="display: contents"></collection-menu>
				} @else if(!this.brookeService.currentItem()) {
					<div class="px-4"></div>
				}
				<panel style="display: contents"></panel>
			</div>
		</div>
		`,
    standalone: true,
    imports: [TopBarComponent, PanelComponent, CollectionMenuComponent]
})
export class AppComponent {
  title = 'brooke';

	public brookeService: BrookeService = inject(BrookeService);


}
