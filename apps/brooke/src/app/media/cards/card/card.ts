import {
  Component,
  ViewEncapsulation,
} from '@angular/core';

@Component({
  selector: 'card',
  imports: [],
  template: `
    <div
      class="card elv lvl1 flex flex-column flex-gap width-min-content"
    >
      <ng-content
        class="card-header"
        select="card-header"
      ></ng-content>

      <ng-content select="card-body"></ng-content>
      <ng-content select="card-actions"></ng-content>
    </div>
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class Card {}
