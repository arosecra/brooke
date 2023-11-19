import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from 'brooke-state';

@Component({
    selector: 'job-details',
    templateUrl: './job-details.component.html',
    standalone: true
})
export class JobDetailsComponent {

	public brookeService: BrookeService = inject(BrookeService)

}
