import { Component, inject, input } from '@angular/core';
import { AppComponent } from '../app.component';
import { Category } from '../model/category';
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
  app = inject(AppComponent);

	category = input.required<Category>()

}
