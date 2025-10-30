import { Component, inject, ViewEncapsulation } from '@angular/core';
import { App } from '../app';

@Component({
  selector: 'series',
  imports: [],
  template: `
    <ul>
      <!-- @for (childItem of app.appState.currentSeries()?.childItems; track childItem.name) {
		<li>
			<a class="button menu-label" (click)="app.openItem(childItem)">{{childItem.name.replaceAll('_', ' ')}}</a>
		</li>
	} -->
    </ul>

    <div>
      <img
        style="width: 100%"
        src="/rest/large-thumbnail/{{ app.appState()?.currentCollection()?.name }}/{{
          app.appState()?.currentSeries()?.name
        }}"
      />
    </div>
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class Series {
  app = inject(App);
}
