import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../app.component';
import { AppContextProvider } from '../../providers/app-context-provider';

@Component({
  selector: 'close-action',
  imports: [MatButtonModule, MatIconModule],
  template: `
    <button matMiniFab title="Close" (click)="app.openHome()" [disabled]="!app.currentCollection()">
      <mat-icon fontSet="material-symbols-outlined">close</mat-icon>
    </button>
  `,
  styles: ``,
})
export class CloseActionComponent {
  app = inject(AppContextProvider).app;
}
