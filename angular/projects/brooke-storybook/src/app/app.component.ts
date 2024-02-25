import { Component } from '@angular/core';
import { IconComponent } from './icon/icon.component';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
	standalone: true,
	imports: [IconComponent, RouterOutlet],
  templateUrl: './app.component.html'
})
export class AppComponent {

}
