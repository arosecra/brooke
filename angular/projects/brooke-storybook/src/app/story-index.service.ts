import { Injectable, signal } from '@angular/core';

export declare interface StorySection {
	title: string;
	substories: StorySubsection[];
}

export declare interface StorySubsection {
	title: string;
}

@Injectable({
  providedIn: 'root'
})
export class StoryIndexService {

  constructor() { }

	public storyIndexes: StorySection[] = [
		{
			title: "Visual Style",
			substories: [
				//Color Typography Size and Spacing Shadows
			]
		},
		{
			title: "Design System Components",
			substories: [
				{
					title: "Colors"
				},
				{
					title: "Icons"
				},
				{
					title: "Buttons",
				},
				{
					title: "Button Groups"
				}
			]
		},
		{
			title: "Design System Utilities",
			substories: [

			]
		},
		{
			title: "Pipes",
			substories: [

			]
		},
		{
			title: "Components",
			substories: [

			]
		},
		{
			title: "Pages",
			substories: [

			]
		}, 
		{
			title: "Stories",
			substories: [

			]
		}
	]

}
