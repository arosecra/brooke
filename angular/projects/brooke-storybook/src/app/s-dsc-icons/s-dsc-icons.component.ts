import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'app-s-dsc-icons',
  standalone: true,
  imports: [CommonModule, IconComponent],
  templateUrl: './s-dsc-icons.component.html',
	styles: [
		':host {display:contents; }'
	]
})
export class SDSCIconsComponent {

}
