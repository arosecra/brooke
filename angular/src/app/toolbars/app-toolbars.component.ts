import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { ActionsModule } from '../actions/actions.module';
import { AppComponent } from '../app.component';
import { BookToCComponent } from '../media/book-toc/book-toc.component';
import { MatChipAndRemoveComponent } from "../shared/mat-chip-and-remove.component";

@Component({
  selector: 'app-toolbars',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    BookToCComponent,
    MatSidenavModule,
    ActionsModule,
    MatChipAndRemoveComponent
	],
  templateUrl: './app-toolbars.component.html',
  styleUrls: ['./app-toolbars.component.scss'],

})
export class AppToolbarsComponent {
  protected app = inject(AppComponent);
}
