import { Component, computed, inject } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { AppComponent } from '../../app.component';
import { VirtualPageComponent } from '../virtual-page/virtual-page.component';

@Component({
  selector: 'lectern',
  imports: [
    MatCardModule,
    VirtualPageComponent,
  ],
  host: {
    '[class.one-page]': 'onePage() && !app.widgets()?.book?.sideBySide()',
    '[class.two-page]': '!onePage() || app.widgets()?.book?.sideBySide()',
  },
  templateUrl: './lectern.component.html',
  styleUrls: ['./lectern.component.scss'],
})
export class BookComponent {
  app = inject(AppComponent);

  onePage = computed<boolean>(() => {
    return this.app.widgets().book.pagesInDisplay() === 1;
  });

	showRightPage = computed<boolean>(() => {
		const bookLength = (this.app.resources().bookCbt.value()?.length ?? 0);
		return this.app.widgets().book.sideBySide() || 
			(this.app.widgets().book.pagesInDisplay() === 2 && 
				bookLength > this.rightPageNo()
			);
	});
	
  leftPageNo = computed<number>(() => {
		return this.app.appState().currentPageSet() * this.app.widgets().book.pagesInDisplay();
	});

	rightPageNo = computed<number>(() => {
		return this.leftPageNo() + 1;
	});
}
