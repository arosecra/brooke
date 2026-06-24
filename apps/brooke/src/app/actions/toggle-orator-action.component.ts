import { Component, inject } from '@angular/core';
import { Orator } from '../../shared';
import { ActionComponent } from './action.component';
import { AppContextProvider } from '../../providers/app-context-provider';

@Component({
  selector: 'toggle-orator-action',
  imports: [ActionComponent],
  template: `
    @if (!orator.reading) {
      <action
        [m]="app.textToSpeech"
        title="Text to Speech"
        [disabled]="orator.reading"
        >text_to_speech</action
      >
    } @else {
      <action [m]="app.stopTextToSpeech" title="Stop Text to Speech" [disabled]="!orator.reading"> stop </action>
    }
  `,
  styles: ``,
})
export class ToggleOratorComponent {
  app = inject(AppContextProvider).app;
  orator = inject(Orator);
}
