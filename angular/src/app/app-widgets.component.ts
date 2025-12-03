import { Component, computed, inject, linkedSignal, signal } from '@angular/core';
import { AppComponent } from './app.component';
import { mqSignal } from './shared/signals/mq-signal';

@Component({
  selector: 'app-widgets',
  imports: [],
  template: ``,
  styles: ``,

  providers: [],
})
export class AppWidgetsComponent {
  private app = inject(AppComponent);

  busy = signal<boolean>(false);
  fullscreen = signal<boolean>(false);

  isMobile = mqSignal('(width <= 600px)');

  panel = {
    showBook: computed(() => {
      return (
        this.app.appState().currentItem() &&
        this.app.appState().currentCollection()?.openType === 'book' &&
        this.app.resources().bookCbt.hasValue()
      );
    }),

    showSettings: computed(() => {
      return !!this.app.appState().showSettingsManual() || !!this.app.appState().showSettingsRequired();
    }),

    showLibrarySettings: computed(() => this.app.appState().showLibraryEditorManual()),
  };
  book = {
    pagesInDisplay: linkedSignal<number>(() => (this.isMobile() ? 1 : 2)),
    thumbnailView: signal<boolean>(false),
    markdownView: signal<boolean>(false),
    sideBySide: signal<boolean>(false),
  };
  sideNav = {
    showOptions: signal<boolean>(false),
    showToc: signal<boolean>(false),
    showTocAdd: signal<boolean>(false),
  };
  sideNavOpen = computed<boolean>(() => {
    return this.sideNav.showOptions() || this.sideNav.showToc() || this.sideNav.showTocAdd();
  });
}
