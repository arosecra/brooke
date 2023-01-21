import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, switchMap, forkJoin, map, repeat, delay } from 'rxjs';
import { Category, Collection, JobDetails } from '../brooke.model';
import { BrookeService } from '../brooke.service';
import { CollectionComponent } from '../collection/collection.component';

interface JobDetailsVM {
	jobDetails: JobDetails;
	queryParams: JobDetailsQueryParams;
}

interface JobDetailsQueryParams {
	collection: string;
	category: string;
	series: string;
	item: string;
	leftPage: string;
	rightPage: string;
}

@Component({
	selector: 'app-job-details',
	templateUrl: './job-details.component.html'
})
export class JobDetailsComponent {

	vm$: Observable<JobDetailsVM>;

	constructor(
		private activatedRoute: ActivatedRoute,
		private brookeService: BrookeService,
		private router: Router,
	) {
	}

	ngOnInit() {

		this.vm$ = this.activatedRoute.queryParams.pipe(
			switchMap((queryParams) => {
				return this.brookeService.getJobDetails(queryParams['jobNumber']).pipe(
					map((jobDetails) => {
						let newQueryParams: any = {...queryParams}
						newQueryParams.jobNumber = undefined;
						newQueryParams.navigateTo = undefined;

						if (jobDetails.total > 0 && jobDetails.total === jobDetails.current) {
							//job is done
							this.router.navigate([queryParams['navigateTo']], {
								queryParams: newQueryParams
							})
						}

						return {
							jobDetails: jobDetails,
							queryParams: newQueryParams
						} as JobDetailsVM;
					}),
					delay(500),
					repeat()
				)
			}
			)
		)
	}
}
