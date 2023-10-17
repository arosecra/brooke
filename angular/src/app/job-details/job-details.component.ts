import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from '../brooke.service';

@Component({
  selector: 'job-details',
  templateUrl: './job-details.component.html'
})
export class JobDetailsComponent {

	public brookeService: BrookeService = inject(BrookeService)

}
