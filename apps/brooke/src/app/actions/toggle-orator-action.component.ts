import { Component, inject } from '@angular/core';
import { AppComponent } from '../app.component';
import { Orator } from '../audio/orator';
import { ActionComponent } from './action.component';

@Component({
  selector: 'toggle-orator-action',
  imports: [ActionComponent],
  template: `
    @if (!orator.reading) {
      <action
        [m]="app.textToSpeech"
        title="Text to Speech"
        [disabled]="orator.reading"
        [disabled]="!app.widgets().panel.showBook() || app.widgets().book.thumbnailView()"
        >text_to_speech</action
      >
    } @else {
      <action [m]="app.stopTextToSpeech" title="Stop Text to Speech" [disabled]="!orator.reading"> stop </action>
    }
  `,
  styles: ``,
})
export class ToggleOratorComponent {
  app = inject(AppComponent);
  orator = inject(Orator);
}
