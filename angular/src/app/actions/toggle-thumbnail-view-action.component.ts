import { Component, inject, input } from '@angular/core';
import { AppComponent } from '../app.component';
import { ActionComponent } from './action.component';
import { MatBadgeModule } from '@angular/material/badge';
import { ItemRef } from '../model/item-ref';

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
