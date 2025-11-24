import { Component, inject, viewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorIntl, MatPaginatorModule } from '@angular/material/paginator';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AppComponent } from '../app.component';
import { Orator } from '../audio/orator';
import { BookToCComponent } from '../media/book-toc/book-toc.component';
import { ActionComponent } from '../actions/action.component';
import { CustomPaginatorIntl } from './custom-paginator-intl.service';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatChipAndRemoveComponent } from '../shared/mat-chip-and-remove.component';
import { ToggleFullscreenActionComponent } from "../actions/toggle-fullscreen-action.component";
import { ToggleOratorComponent } from "../actions/toggle-orator-action.component";
import { ToggleBookOptionsComponent } from "../actions/toggle-book-options-action.component";
import { ActionsModule } from '../actions/actions.module';
import { ToggleAddToCComponent } from "../actions/toggle-add-toc-action.component";

@Component({
  selector: 'app-toolbars',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatPaginatorModule,
    BookToCComponent,
    ActionComponent,
    MatSidenavModule,
    MatChipAndRemoveComponent,
    ActionsModule,
    ToggleAddToCComponent
],
  providers: [{ provide: MatPaginatorIntl, useClass: CustomPaginatorIntl }],
  templateUrl: './app-header.component.html',
  styleUrls: ['./app-header.component.scss'],

})
export class AppToolbarsComponent {
  protected app = inject(AppComponent);
	protected orator = inject(Orator);
}
