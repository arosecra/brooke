import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'story-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './story-section.component.html',
	styles: [
		':host {display:contents; }'
	]
})
export class StorySectionComponent {

}
