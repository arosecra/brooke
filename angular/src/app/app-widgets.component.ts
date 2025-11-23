import { Component, computed, inject, linkedSignal, signal, ViewEncapsulation } from '@angular/core';
import { AppComponent } from './app.component';
import { BreakpointObserver, Breakpoints, BreakpointState } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-widgets',
  imports: [],
  template: ``,
  styles: ``,

  providers: [],
})
export class AppWidgetsComponent {
  private app = inject(AppComponent);
	private breakpointObserver = inject(BreakpointObserver);

	busy = signal<boolean>(false);
	fullscreen = signal<boolean>(false);
	
	isMobile = toSignal(
    this.breakpointObserver.observe([Breakpoints.Handset, Breakpoints.Small])
			.pipe(
				map((breakpointState: BreakpointState) => {
					return breakpointState.matches
				})
			)		
		,
    { initialValue: false }
  )

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
    pagesInDisplay: linkedSignal<number>(() => this.isMobile() ? 1 : 2),
    thumbnailView: signal<boolean>(false),
    markdownView: signal<boolean>(false),
    sideBySide: signal<boolean>(false),
  };
}
