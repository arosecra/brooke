import { ViewportScroller } from '@angular/common';
import { AfterViewInit, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AppContextProvider } from '../../../providers/app-context-provider';
import { Page, PageType } from '../../../shared';

@Component({
  selector: 'thumbnail-gallery',
  imports: [
    MatCardModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatIconModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './thumbnail-gallery.component.html',
  styleUrls: ['./thumbnail-gallery.component.scss'],

})
export class ThumbnailGalleryComponent implements AfterViewInit {
  scroller = inject(ViewportScroller)

  ngAfterViewInit(): void {
    setTimeout(() => {
      window.scrollTo({
        top: 0,
        left: 0,
        behavior: 'smooth'
      });

    }, 10000)

  }
  app = inject(AppContextProvider).app;

  cycleImageType(page: Page, $event: any) {
    $event.preventDefault();
    const types: PageType[] = ['Text', 'Image', 'Blank'];
    let idx = types.findIndex((type) => page.type === type);
    idx = (idx + 1) % types.length;
    page.type = types[idx];
  }
}
