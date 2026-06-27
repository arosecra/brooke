import {
  Component,
  inject,
  ViewEncapsulation,
} from '@angular/core';
import { AppContextProvider } from '../providers/app-context-provider';
import { ButtonComponent } from '../shared';
import { OratorContextProvider } from '../providers/orator-context-provider';

@Component({
  selector: 'AppToolbar',
  imports: [ButtonComponent],
  template: `
    <aside>
      <Btn
        type="fab"
        class="margin-b"
        (click)="app.toggleMenu()"
        [icon]="app.menu() ? 'menu_open' : 'menu'"
      />
      @if (app.fullscreen()) {
        <Btn
          type="fab"
          label="Minimize"
          class="margin-b"
          icon="fullscreen_exit"
          (click)="app.toggleFullScreen()"
        />
      } @else {
        <Btn
          type="fab"
          label="Maximize"
          class="margin-b"
          icon="fullscreen"
          (click)="app.toggleFullScreen()"
        />
      }
      @if (orator.reading) {
        <Btn
          type="fab"
          label="Stop Read"
          class="margin-b"
          icon="stop"
          (click)="app.stopTextToSpeech()"
        />
      } @else {
        <Btn
          type="fab"
          label="Read"
          class="margin-b"
          icon="text_to_speech"
          [disabled]="
            !app.location().item ||
            app.location().collection?.openType !== 'book'
          "
          (click)="app.textToSpeech()"
        />
      }
      <Btn
        type="fab"
        label="Options"
        class="margin-b"
        icon="book_2"
      />
      <Btn
        type="fab"
        label="TOC"
        class="margin-b"
        icon="format_list_bulleted"
        (click)="app.openTOC()"
      />
      <Btn
        type="fab"
        label="TOC+"
        class="margin-b"
        icon="format_list_bulleted_add"
      />
      <Btn
        type="fab"
        label="To Top"
        class="margin-b"
        icon="arrow_circle_up"
      />
      <Btn
        type="fab"
        label="Close"
        class="margin-b"
        icon="close"
        (click)="app.openHome()"
      />
    </aside>
    @if (app.menu()) {
      <aside class="book-options">
        <div class="fab margin"></div>
        <button
          type="button"
          title="Markdown"
          class="fab margin"
        >
          <span class="icon icon-font">markdown</span>
        </button>
        <button
          type="button"
          title="Thumbnails"
          class="fab margin"
        >
          <span class="icon icon-font">dataset</span>
        </button>
        <button
          type="button"
          title="Page Mode"
          class="fab margin"
        >
          <span class="icon icon-font">two_pager</span>
        </button>
        <button
          type="button"
          title="Compare"
          class="fab margin"
        >
          <span class="icon icon-font">compare</span>
        </button>
      </aside>
    }
  `,
  encapsulation: ViewEncapsulation.None,
})
export class AppToolbarComponent {
  app = inject(AppContextProvider).app;
  orator = inject(OratorContextProvider).orator;
}
