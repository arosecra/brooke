import { Component, inject, ViewEncapsulation } from '@angular/core';

import { DBContextProvider } from './db-context-provider';
import { App } from '../shared/services/App';


@Component({
  selector: 'app-context-provider',
  imports: [],
  template: `<ng-content/>`,
  encapsulation: ViewEncapsulation.None,
})
export class AppContextProvider {
  appDB = inject(DBContextProvider);
  app: App;

  constructor() {
    this.app = new App(this.appDB.appDB);

  }
}
