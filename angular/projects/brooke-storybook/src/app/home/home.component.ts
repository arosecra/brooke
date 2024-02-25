import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { StoryIndexService, StorySection, StorySubsection } from '../story-index.service';
import { IconComponent } from '../icon/icon.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, IconComponent],
  templateUrl: './home.component.html',
	styles: [
		':host {display:contents; }'
	]
})
export class HomeComponent {
	protected storyIndexService = inject(StoryIndexService);

	private router: Router = inject(Router);
	
	setSelectedStory(storySection?: StorySection, storySubsection?: StorySubsection) {
		this.router.navigateByUrl(`/story-section/${storySection?.title}/${storySubsection?.title}`)
	}
}
