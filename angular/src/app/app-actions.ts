import { Component, computed, inject, signal, ViewEncapsulation } from '@angular/core';
import { App } from './app';

@Component({
  selector: 'app-actions',
  imports: [],
  template: ``,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
  providers: [],
})
export class AppActions {
  private app = inject(App);

}
