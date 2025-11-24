import { Component, inject } from '@angular/core';
import { AppComponent } from '../app.component';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-side-by-side-action',
  imports: [ActionComponent],
  template: `
    <action [m]="app.toggleSideBySide" title="Compare Markdown and Image" [disabled]="app.widgets().isMobile()"
      >compare</action
    >
  `,
  styles: ``,
})
export class ToggleSideBySideComponent {
  app = inject(AppComponent);
}
