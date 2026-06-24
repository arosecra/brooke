import { Component, inject } from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-num-pages-action',
  imports: [ActionComponent],
  template: `
    <action [m]="app.toggleOneOrTwoPageMode" title="Toggle Page Mode" [disabled]="app.isMobile()"
      >two_pager</action
    >
  `,
  styles: ``,
})
export class ToggleNumPagesComponent {
  app = inject(AppContextProvider).app;
}
