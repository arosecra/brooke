import { Component, computed, inject, signal, ViewEncapsulation } from '@angular/core';
import { App } from './app';

@Component({
  selector: 'app-widgets',
  imports: [],
  template: ``,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
  providers: [],
})
export class AppWidgets {
  private app = inject(App);

	busy = signal<boolean>(false)

  panel = {
    showBook: computed(() => {
      return this.app.appState().currentItem() 
				&& this.app.appState().currentCollection()?.openType === 'book'
				&& this.app.resources().bookCbt.hasValue();
    }),

    showSeries: computed(() => {
      return !!this.app.appState().currentSeries();
    }),

    showSettings: computed(() => {
      return !!this.app.appState().showSettingsManual() || !!this.app.appState().showSettingsRequired();
    }),

    showLibrarySettings: computed(() => this.app.appState().showLibraryEditorManual()),

    showLoading: computed(() => {
      return this.app.resources().storedLibrary.isLoading();
    }),
  };
  book = {
    pagesInDisplay: signal<number>(2),
    thumbnailView: signal<boolean>(false),
    markdownView: signal<boolean>(false),
    sideBySide: signal<boolean>(false),
  };
}
