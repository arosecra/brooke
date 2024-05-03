import { Component, inject } from '@angular/core';
import { TopBarComponent } from './top-bar/top-bar.component';
import { PanelComponent } from './panel/panel.component';
import { BrookeService } from './brooke.service';
import { CollectionMenuComponent } from "./collection-menu/collection-menu.component";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    standalone: true,
    imports: [TopBarComponent, PanelComponent, CollectionMenuComponent]
})
export class AppComponent {
  title = 'brooke';

	public brookeService: BrookeService = inject(BrookeService);


}
