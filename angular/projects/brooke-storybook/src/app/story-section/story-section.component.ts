import { CommonModule } from '@angular/common';
import { Component, Input, inject } from '@angular/core';
import { StoryIndexService, StorySection, StorySubsection } from '../story-index.service';

@Component({
  selector: 'story-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './story-section.component.html',
	styles: [
		':host {display:contents; }'
	]
})
export class StorySectionComponent {
	@Input() sectionName: string;
	@Input() subSectionName: string;

	protected storyIndexService = inject(StoryIndexService);

	
	protected storySection: StorySection;
	protected storySubSection: StorySubsection;

	ngOnChanges() {
		this.storyIndexService.storyIndexes.forEach((idx) => {
			if(idx.title === this.sectionName) {
				this.storySection = idx;
				idx.substories.forEach((sidx) => {
					if(sidx.title === this.subSectionName) {
						this.storySubSection = sidx;
					}
				})
			}
		})
	}
}
