import { Component, inject } from '@angular/core';
import { BrookeService } from './brooke.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class App {
  title = 'brooke';

	public brookeService: BrookeService = inject(BrookeService)

}
