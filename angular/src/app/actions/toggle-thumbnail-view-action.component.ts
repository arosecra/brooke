import { Component, inject } from '@angular/core';
import { AppComponent } from '../app.component';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-thumbnail-view-action',
  imports: [ActionComponent],
  template: ` 
	<action [m]="app.toggleThumbnailView" title="View Thumbnails" [disabled]="app.widgets().isMobile()"
    >dataset
  </action>`,
  styles: ``,
})
export class ToggleThumbnailViewActionComponent {
  app = inject(AppComponent);
}
