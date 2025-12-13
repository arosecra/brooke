import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../app.component';

@Component({
  selector: 'close-action',
  imports: [MatButtonModule, MatIconModule],
  template: `
    <button matMiniFab title="Close" (click)="app.openHome()" [disabled]="!app.appState().currentCollection()">
      <mat-icon fontSet="material-symbols-outlined">close</mat-icon>
    </button>
  `,
  styles: ``,
})
export class CloseActionComponent {
  app = inject(AppComponent);
}
