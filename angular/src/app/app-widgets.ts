import { Component, computed, inject, signal, ViewEncapsulation } from '@angular/core';
import { AppComponent } from './app';

@Component({
  selector: 'app-widgets',
  imports: [],
  template: ``,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
  providers: [],
})
export class AppWidgetsComponent {
  private app = inject(AppComponent);

	busy = signal<boolean>(false)

  panel = {
    showBook: computed(() => {
      return this.app.appState().currentItem() 
				&& this.app.appState().currentCollection()?.openType === 'book'
				&& this.app.resources().bookCbt.hasValue();
    }),

    showSettings: computed(() => {
      return !!this.app.appState().showSettingsManual() || !!this.app.appState().showSettingsRequired();
    }),

    showLibrarySettings: computed(() => this.app.appState().showLibraryEditorManual())
  };
  book = {
    pagesInDisplay: signal<number>(2),
    thumbnailView: signal<boolean>(false),
    markdownView: signal<boolean>(false),
    sideBySide: signal<boolean>(false),
  };
}
