import { Component, inject } from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-fullscreen-action',
  imports: [ActionComponent],
  template: `
    <action [m]="app.toggleFullScreen" title="Fullscreen">
      @if (app.fullscreen()) {
        fullscreen_exit
      } @else {
        fullscreen
      }
    </action>
  `,
  styles: ``,
})
export class ToggleFullscreenActionComponent {
  app = inject(AppContextProvider).app;
}
