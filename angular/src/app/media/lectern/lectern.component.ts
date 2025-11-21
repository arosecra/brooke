import { Component, inject, ViewEncapsulation } from '@angular/core';
import { AppComponent } from '../../app.component';

import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Page, PageType } from '../../model/page';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { PageComponent } from '../page/page.component';

@Component({
  selector: 'lectern',
  imports: [MatCardModule, MatButtonModule, MatButtonToggleModule, MatIconModule, FormsModule, MatFormFieldModule, MatInputModule, PageComponent],
	host: {
		'[class.one-page]': '!app.widgets()?.book?.thumbnailView() && app.widgets()?.book?.pagesInDisplay() === 1 && !app.widgets()?.book?.sideBySide()',
		'[class.two-page]': '!app.widgets()?.book?.thumbnailView() && (app.widgets()?.book?.pagesInDisplay() === 2 || app.widgets()?.book?.sideBySide())',
	},
  templateUrl: './lectern.component.html',
  styleUrls: ['./lectern.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class BookComponent {
  app = inject(AppComponent);

	cycleImageType(page: Page) {
		const types: PageType[] = ['Text', 'Image', 'Blank'];
		let idx = types.findIndex((type) => page.type === type);
		idx = (idx + 1) % types.length;
		page.type = types[idx];
	}
}

