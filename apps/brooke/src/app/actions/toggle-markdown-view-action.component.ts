import { Component, inject } from '@angular/core';
import { AppComponent } from '../app.component';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-markdown-view-action',
  imports: [ActionComponent],
  template: ` <action [m]="app.toggleMarkdownView" title="View Markdown"> markdown </action> `,
  styles: ``,
})
export class ToggleMarkdownViewActionComponent {
  app = inject(AppComponent);
}
