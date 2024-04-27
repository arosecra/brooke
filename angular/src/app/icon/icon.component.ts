import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'icon',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './icon.component.html'
})
export class IconComponent {
	@Input() name: 'bookshelf' | 'greater-than' | 'less-than' | 'home' | 'hamburger';
}
