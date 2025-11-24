import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../app.component';

@Component({
  selector: 'toggle-add-toc-action',
  imports: [MatButtonModule, MatIconModule],
  template: `
		<button matMiniFab title="Add ToC Entry" (click)="app.toggleAddToC()"> <!-- add to book toc -->
			<mat-icon fontSet="material-symbols-outlined">format_list_bulleted_add</mat-icon>
		</button>
  `,
  styles: ``,
})
export class ToggleAddToCComponent {
  app = inject(AppComponent);
}
