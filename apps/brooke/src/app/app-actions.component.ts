import { Component, inject } from '@angular/core';
import { AppContextProvider } from '../providers/app-context-provider';

@Component({
  selector: 'app-actions',
  imports: [],
  template: ``,
  styles: ``,

  providers: [],
})
export class AppActionsComponent {
  private app = inject(AppContextProvider).app;

}
