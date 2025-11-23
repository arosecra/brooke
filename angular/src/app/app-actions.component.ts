import { Component, computed, inject, signal } from '@angular/core';
import { AppComponent } from './app.component';

@Component({
  selector: 'app-actions',
  imports: [],
  template: ``,
  styles: ``,

  providers: [],
})
export class AppActionsComponent {
  private app = inject(AppComponent);

}
