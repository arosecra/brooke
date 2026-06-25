import { Component, computed, inject } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { AppContextProvider } from '../../../providers/app-context-provider';
import { VirtualPageComponent } from '../virtual-page/virtual-page.component';

@Component({
  selector: 'lectern',
  imports: [MatCardModule, VirtualPageComponent],
  host: {
    '[class.one-page]':
      'onePage() && app.bookViewMode() !== "COMPARE"',
    '[class.two-page]':
      '!onePage() || app.bookViewMode() === "COMPARE"',
    '(document:keydown.arrowRight)':
      'app.goToNextPage($event)',
    '(document:keydown.arrowLeft)':
      'app.goToPreviousPage($event)',
    '(document:pointerdown)': 'app.onTouchStart($event)',
  },
  templateUrl: './lectern.component.html',
  styleUrls: ['./lectern.component.scss'],
})
export class BookComponent {
  app = inject(AppContextProvider).app;

  onePage = computed<boolean>(() => {
    return this.app.pageMode() === 'ONE_PAGE';
  });

  showRightPage = computed<boolean>(() => {
    const bookLength =
      this.app.bookCbt.value()?.length ?? 0;
    return (
      this.app.bookViewMode() === 'COMPARE' ||
      this.app.bookViewMode() === 'MARKDOWN' ||
      (this.app.bookViewMode() === 'IMAGE' &&
        bookLength > this.rightPageNo())
    );
  });

  leftPageNo = computed<number>(() => {
    return this.app.location().pageSet;
  });

  rightPageNo = computed<number>(() => {
    return this.leftPageNo() + 1;
  });
}
