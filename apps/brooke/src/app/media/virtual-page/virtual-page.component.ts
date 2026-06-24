import { Component, inject, input, OnDestroy, OnInit } from '@angular/core';
import { RemarkModule } from 'ngx-remark';
import { AppContextProvider } from '../../../providers/app-context-provider';
import { Page } from '../../../shared';

@Component({
	selector: 'virtual-page',
	imports: [RemarkModule],
	templateUrl: './virtual-page-component.html',
	styleUrls: ['./virtual-page-component.scss'],

})
export class VirtualPageComponent implements OnInit, OnDestroy {
	app = inject(AppContextProvider).app;

	page = input.required<Page>();
	preferMarkdown = input<boolean>(false);

	imageUrl: string;

	ngOnInit(): void {
		// this.imageUrl = URL.createObjectURL(this.page().fullPage);

	}
	ngOnDestroy(): void {
		if (this.imageUrl) URL.revokeObjectURL(this.imageUrl);
	}
}
