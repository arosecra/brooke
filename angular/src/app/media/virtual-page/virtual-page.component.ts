import { Component, inject, input, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { AppComponent } from '../../app.component';
import { Page } from '../../model/page';
import { RemarkModule } from 'ngx-remark';

@Component({
  selector: 'virtual-page',
  imports: [RemarkModule],
  template: `
		@let imgMode = !preferMarkdown() || !page().markdown;

		@if(page().type === 'Blank') {
			<div> Intentionally left blank </div>
		} @else if (imgMode) {
			<img style="width: 100%" [src]="page().fullPage"
			/>
		} @else {
			<remark [markdown]="page().markdown">
				<div *remarkTemplate="'paragraph'; let node" [remarkNode]="node"></div>
				<p *remarkTemplate="'text'; let node">{{ node.value }}</p>
			</remark>
		}
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class VirtualPageComponent implements OnInit, OnDestroy {
  app = inject(AppComponent);

	page = input.required<Page>();
	preferMarkdown = input<boolean>(false);

	imageUrl: string;

	ngOnInit(): void {
		// this.imageUrl = URL.createObjectURL(this.page().fullPage);
		
	}
	ngOnDestroy(): void {
		if(this.imageUrl) URL.revokeObjectURL(this.imageUrl);
	}
}
