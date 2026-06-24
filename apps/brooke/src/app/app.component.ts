import { Component } from '@angular/core';
import { AppContextProvider } from "../providers/app-context-provider";
import { DBContextProvider } from "../providers/db-context-provider";
import { OratorContextProvider } from "../providers/orator-context-provider";
import { AppMainPanelComponent } from "./app-main-panel.component";
import { AppBarComponent } from "./app-bar.component";
import { AppToolbarComponent } from "./app-toolbar.component";

@Component({
  selector: 'app',
  imports: [
    OratorContextProvider,
    DBContextProvider,
    AppContextProvider,
    AppMainPanelComponent,
    AppBarComponent,
    AppToolbarComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],

  providers: [],
  // host: {
  // '(document:keydown.arrowRight)': 'app.app.goToNextPage($event)',
  // '(document:keydown.arrowLeft)': 'app.app.goToPreviousPage($event)',
  // '(document:pointerdown)': 'app.app.onTouchStart($event)',
  // },
})
export class AppComponent {

  constructor() {
    // const loc = this.location.path().split('/');
    // if(loc.length > 0) {
    // }
  }
}
