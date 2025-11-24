import { Component, inject } from '@angular/core';
import { AppComponent } from '../app.component';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-fullscreen-action',
  imports: [ActionComponent],
  template: `
    <action [m]="app.toggleFullScreen" title="Fullscreen">
      @if (app.widgets().fullscreen()) {
        fullscreen_exit
      } @else {
        fullscreen
      }
    </action>
  `,
  styles: ``,
})
export class ToggleFullscreenActionComponent {
  app = inject(AppComponent);
}
