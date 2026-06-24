import { Component, inject, ViewEncapsulation } from '@angular/core';

import { OratorContextProvider } from './orator-context-provider';
import { AppDB } from '../shared';

@Component({
  selector: 'db-context-provider',
  imports: [],
  template: `<ng-content/>`,
  encapsulation: ViewEncapsulation.None,
})
export class DBContextProvider {
  appDB = new AppDB(inject(OratorContextProvider).orator);
}
