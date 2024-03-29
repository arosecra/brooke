import { Component, inject } from '@angular/core';
import { CommonModule, NgStyle } from '@angular/common';
import { BrookeService } from 'brooke-state';
import { CategoryAndSeriesBrowserComponent } from '../category-and-series-browser/category-and-series-browser.component';
import { SeriesComponent } from '../series/series.component';
import { BookComponent } from '../book/book.component';
import { CollectionMenuComponent } from '../collection-menu/collection-menu.component';

@Component({
    selector: 'panel',
    templateUrl: './panel.component.html',
    standalone: true,
    imports: [CollectionMenuComponent, NgStyle, BookComponent, SeriesComponent, CategoryAndSeriesBrowserComponent]
})
export class PanelComponent {
	public brookeService: BrookeService = inject(BrookeService)

}
