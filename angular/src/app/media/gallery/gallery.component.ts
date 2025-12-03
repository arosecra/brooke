import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AppComponent } from '../../app.component';
import { Page, PageType } from '../../model/page';

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

  cycleImageType(page: Page, $event: any) {
    const types: PageType[] = ['Text', 'Image', 'Blank'];
    let idx = types.findIndex((type) => page.type === type);
    idx = (idx + 1) % types.length;
    page.type = types[idx];
		$event.stopPropagation();
  }
}
