import { Component, inject } from '@angular/core';
import { BrookeService } from './brooke.service';
import { PanelComponent } from './panel/panel.component';
import { TopBarComponent } from './top-bar/top-bar.component';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    standalone: true,
    imports: [TopBarComponent, PanelComponent]
})
export class App {
  title = 'brooke';

	public brookeService: BrookeService = inject(BrookeService)

}
