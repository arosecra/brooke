import { Component, inject } from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-side-by-side-action',
  imports: [ActionComponent],
  template: `
    <action [m]="app.openCompare" title="Compare Markdown and Image" [disabled]="app.isMobile()"
      >compare</action
    >
  `,
  styles: ``,
})
export class ToggleSideBySideComponent {
  app = inject(AppContextProvider).app;
}
