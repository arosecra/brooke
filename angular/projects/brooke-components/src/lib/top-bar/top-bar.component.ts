import { Component, inject } from '@angular/core';
import { BrookeService } from 'brooke-state';
import { BookPageTurnerComponent } from '../book-page-turner/book-page-turner.component';
import { BookOptionsComponent } from '../book-options/book-options.component';
import { BookTocComponent } from '../book-toc/book-toc.component';
import { ModifyCollectionButtonComponent } from '../modify-collection-button/modify-collection-button.component';
import { BreadcrumbComponent } from '../breadcrumb/breadcrumb.component';

@Component({
    selector: 'top-bar',
    templateUrl: './top-bar.component.html',
    standalone: true,
    imports: [BreadcrumbComponent, ModifyCollectionButtonComponent, BookTocComponent, BookOptionsComponent, BookPageTurnerComponent]
})
export class TopBarComponent {
	public brookeService: BrookeService = inject(BrookeService)

	onToggleAsideMenuButtonClick() {
		this.brookeService.widgets.asideMenuExpanded.update(() => !this.brookeService.widgets.asideMenuExpanded())
	}
}
