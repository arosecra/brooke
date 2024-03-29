import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'icon',
	standalone: true,
  templateUrl: './icon.component.html',
	styles: [
		':host {display:contents; }'
	]
})
export class IconComponent {

	@Input() name: string = "scholar-cap";

}
