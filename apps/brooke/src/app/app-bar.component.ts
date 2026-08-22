import { CommonModule } from '@angular/common';
import {
  Component,
  inject,
  ViewEncapsulation,
} from '@angular/core';
import { AppContextProvider } from '../providers/app-context-provider';
import {
  ButtonComponent,
  SearchComponent,
} from '../shared';

@Component({
  selector: 'AppBar',
  imports: [CommonModule, SearchComponent, ButtonComponent],
  template: `
    @if (!app.fullscreen()) {
      <header class="appbar">
        <div class="appbar-left">
          <div>
            <h1 class="appbar-title">Brooke</h1>
            <div class="appbar-version">version</div>
          </div>
          @if (app.mainPanelMode() !== 'SETTINGS') {
            <Search></Search>
          }
        </div>
        <div class="appbar-center"></div>
        <div class="appbar-right">
          <Btn
            type="fab"
            icon="settings"
            (click)="app.openSettings()"
          />
        </div>
      </header>
    }
  `,
  encapsulation: ViewEncapsulation.None,
})
export class AppBarComponent {
  app = inject(AppContextProvider).app;
}
