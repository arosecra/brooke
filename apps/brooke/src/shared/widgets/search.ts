import {
  Component,
  inject,
  input,
  ViewEncapsulation,
} from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';

@Component({
  selector: 'Search',
  imports: [],
  template: `
    <div class="search-container">
      <div class="search">
        <div class="icon-font leading-icon">search</div>
        <input
          #searchInput
          type="text"
          [value]="appProvider.app.location().search"
          (input)="
            appProvider.app.search(searchInput.value || '')
          "
          placeholder="Search"
        />
      </div>
    </div>
  `,
  encapsulation: ViewEncapsulation.None,
})
export class SearchComponent {
  appProvider = inject(AppContextProvider);
  disabled = input<boolean>(false);
}
