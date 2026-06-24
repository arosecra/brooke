import { Component, inject } from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-markdown-view-action',
  imports: [ActionComponent],
  template: ` <action [m]="app.openItemMarkdown" title="View Markdown"> markdown </action> `,
  styles: ``,
})
export class ToggleMarkdownViewActionComponent {
  app = inject(AppContextProvider).app;
}
