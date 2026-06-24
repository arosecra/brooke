import {
  Component,
  ViewEncapsulation,
} from '@angular/core';

@Component({
  selector: 'card-actions',
  imports: [],
  template: ` <ng-content></ng-content> `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class CardActions {}
