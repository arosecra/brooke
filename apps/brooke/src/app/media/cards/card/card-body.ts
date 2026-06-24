import {
  Component,
  ViewEncapsulation,
} from '@angular/core';

@Component({
  selector: 'card-body',
  imports: [],
  template: ` <ng-content></ng-content> `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class CardBody {}
