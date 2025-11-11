import { Component, computed, inject, signal, ViewEncapsulation } from '@angular/core';
import { AppComponent } from './app';

@Component({
  selector: 'app-actions',
  imports: [],
  template: ``,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
  providers: [],
})
export class AppActionsComponent {
  private app = inject(AppComponent);

}
