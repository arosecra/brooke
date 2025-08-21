import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'icon',
    imports: [CommonModule],
    // templateUrl: './icon.component.html'
    template: `
@if(name === 'greater-than') {
<svg
  xmlns="http://www.w3.org/2000/svg"
  class="h-3 w-3"
  viewBox="0 0 20 20"
  fill="currentColor"
>
  <path
    fill-rule="evenodd"
    d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
    clip-rule="evenodd"
  />
</svg>
} @else if (name === 'less-than') {
<svg
  xmlns="http://www.w3.org/2000/svg"
  class="h-3 w-3"
  viewBox="0 0 20 20"
  fill="currentColor"
>
  <path
    fill-rule="evenodd"
    d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
    clip-rule="evenodd"
  />
</svg>
} @else if (name === 'home') {
<svg
  xmlns="http://www.w3.org/2000/svg"
  class="h-4 w-4"
  fill="none"
  viewBox="0 0 24 24"
  stroke="currentColor"
>
  <path
    stroke-linecap="round"
    stroke-linejoin="round"
    stroke-width="2"
    d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"
  />
</svg>
} @else if (name === 'bookshelf') {
<svg
  xmlns="http://www.w3.org/2000/svg"
  class="h-4 w-4"
  fill="none"
  viewBox="0 0 24 24"
  stroke="currentColor"
>
  <path
    d="M9 3V18H12V3H9M12 5L16 18L19 17L15 4L12 5M5 5V18H8V5H5M3 19V21H21V19H3Z"
  />
</svg>
} @else if (name === 'hamburger') {
	<svg
        xmlns="http://www.w3.org/2000/svg"
        class="h-5 w-5"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        stroke-width="2"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M4 6h16M4 12h16M4 18h16"
        />
      </svg>
}
	
	`
})
export class IconComponent {
	@Input() name: 'bookshelf' | 'greater-than' | 'less-than' | 'home' | 'hamburger';
}
