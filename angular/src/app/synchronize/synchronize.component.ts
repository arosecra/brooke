import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BrookeService } from '../brooke.service';

@Component({
  selector: 'app-synchronize',
  templateUrl: './synchronize.component.html'
})
export class SynchronizeComponent implements OnInit {

  constructor(
    private activatedRoute: ActivatedRoute,
    private brookeService: BrookeService,
    private router: Router,
  ) {
  }


	ngOnInit(): void {
		this.brookeService.synchronize().subscribe((jobDetails) => {
			this.router.navigate(['/job-details'], {
				queryParams: {
					navigateTo: '/home',
					jobNumber: jobDetails.jobNumber
				}
			})
		});
	}

}
