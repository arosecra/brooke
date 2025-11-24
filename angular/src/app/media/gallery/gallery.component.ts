import { Component, inject } from '@angular/core';
import { AppComponent } from '../../app.component';

import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Page, PageType } from '../../model/page';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'gallery',
  imports: [
    MatCardModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatIconModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './gallery.component.html',
  styleUrls: ['./gallery.component.scss'],

})
export class GalleryComponent {
  app = inject(AppComponent);

  cycleImageType(page: Page) {
    const types: PageType[] = ['Text', 'Image', 'Blank'];
    let idx = types.findIndex((type) => page.type === type);
    idx = (idx + 1) % types.length;
    page.type = types[idx];
  }
}
