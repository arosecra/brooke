import { Component, inject, input } from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';
import { Category } from '../../shared';
import { ActionComponent } from './action.component';

@Component({
  selector: 'select-category-action',
  imports: [ActionComponent],
  template: `
    <action tonal="{{ category().displayName }}" [m]="app.openCategory" [p]="[category()]"></action>
  `,
  styles: ``,
})
export class SelectCategoryActionComponent {
  app = inject(AppContextProvider).app;

  category = input.required<Category>()

}
