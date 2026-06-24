import { Component, inject, input } from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';
import { Collection } from '../../shared';
import { ActionComponent } from './action.component';

@Component({
  selector: 'select-collection-action',
  imports: [ActionComponent],
  template: `
    <action 
			tonal="{{ collection().displayName }}" 
			[m]="app.openCollection" 
			[p]="[collection()]"></action>
  `,
  styles: ``,
})
export class SelectCollectionActionComponent {
  app = inject(AppContextProvider).app;

  collection = input.required<Collection>();
}
