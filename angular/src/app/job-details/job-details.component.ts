import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BrookeService } from '../brooke.service';

@Component({
    selector: 'job-details',
    // templateUrl: './job-details.component.html',
		template: `
		<section class="hero is-large">
	<div class="hero-body">
		<p class="title">
			{{brookeService.currentJob()?.jobDescription}}
		</p>

		<nav class="level">
			<!-- Left side -->
			<div class="level-left">
				<div class="level-item has-text-centered">
					<div>
						<p class="heading">{{brookeService.currentJob()?.currentProgressDescription}}</p>
						<p class="title">{{brookeService.currentJob()?.current}}</p>
					</div>
				</div>
			</div>
		
			<!-- Right side -->
			<div class="level-right">
				<div class="level-item has-text-centered">
					<div>
						<p class="heading">{{brookeService.currentJob()?.totalProgressDescription}}</p>
						<p class="title">{{brookeService.currentJob()?.total}}</p>
					</div>
				</div>
			</div>
		</nav>


		<p class="subtitle">
			<progress class="progress is-large" value="{{brookeService.currentJob()?.current}}" max="{{brookeService.currentJob()?.total}}">80%</progress>
		</p>
	</div>
</section>
		
		`,
    standalone: true
})
export class JobDetailsComponent {

	public brookeService: BrookeService = inject(BrookeService)

}
