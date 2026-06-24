import { Component, inject } from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-thumbnail-view-action',
  imports: [ActionComponent],
  template: ` 
	<action [m]="app.openItemThumbnails" title="View Thumbnails" [disabled]="app.isMobile()"
	
    >dataset
  </action>`,
  styles: ``,
})
export class ToggleThumbnailViewActionComponent {
  app = inject(AppContextProvider).app;
}
