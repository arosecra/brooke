import { Component, ViewEncapsulation } from '@angular/core';
import { Orator } from '../shared';


@Component({
  selector: 'orator-context-provider',
  imports: [],
  template: `<ng-content/>`,
  encapsulation: ViewEncapsulation.None,
})
export class OratorContextProvider {
  orator = new Orator();
}
