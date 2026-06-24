import { Component, signal, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'Search',
  imports: [],
  template: `
  <div class="search-container">
    <div class="search">
      <div class="icon-font leading-icon">search</div>
      <input #searchInput
        type="text"
        [value]="search()"
        (input)="search.set(searchInput.value || '')"
        placeholder="Search"
      />
    </div>
  </div>
  `,
  encapsulation: ViewEncapsulation.None,
})
export class SearchComponent {
  search = signal<string>('');
}
