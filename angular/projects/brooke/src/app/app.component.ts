import { Component, inject } from '@angular/core';
import { BrookeService } from 'brooke-state';
import { PanelComponent } from 'brooke-components';
import { TopBarComponent } from 'brooke-components';

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
