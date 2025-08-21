import { Component, inject } from '@angular/core';
import { BrookeService } from './brooke.service';

@Component({
    selector: 'job-queue',
    imports: [],
    template: `
	<table>
	@for(job of brookeService.allJobs(); track job.jobNumber) {
		<tr>
			<td>{{job.jobNumber}}</td>
			<td>{{job.jobDescription}}</td>
			<td>{{job.total}}</td>
			<td>{{job.current}}</td>
		</tr>
	}
	</table>
  `
})
export class JobQueueComponent {
	public brookeService: BrookeService = inject(BrookeService)

}
