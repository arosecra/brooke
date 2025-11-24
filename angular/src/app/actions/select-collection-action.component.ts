import { Component, inject, input } from '@angular/core';
import { AppComponent } from '../app.component';
import { Collection } from '../model/collection';
import { ActionComponent } from './action.component';

@Component({
  selector: 'select-collection-action',
  imports: [ActionComponent],
  template: `
    <action tonal="{{ collection().name.replaceAll('_', ' ') }}" [m]="app.openCollection" [p]="[collection()]"></action>
  `,
  styles: ``,
})
export class SelectCollectionActionComponent {
  app = inject(AppComponent);

	collection = input.required<Collection>();
}
