import { Component, inject } from '@angular/core';
import { AppComponent } from '../app.component';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-num-pages-action',
  imports: [ActionComponent],
  template: `
    <action [m]="app.toggleOneOrTwoPageMode" title="Toggle Page Mode" [disabled]="app.widgets().isMobile()"
      >two_pager</action
    >
  `,
  styles: ``,
})
export class ToggleNumPagesComponent {
  app = inject(AppComponent);
}
