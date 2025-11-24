import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorIntl, MatPaginatorModule } from '@angular/material/paginator';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { ActionsModule } from '../actions/actions.module';
import { AppComponent } from '../app.component';
import { Orator } from '../audio/orator';
import { BookToCComponent } from '../media/book-toc/book-toc.component';
import { CustomPaginatorIntl } from './custom-paginator-intl.service';
import { MatChipAndRemoveComponent } from "../shared/mat-chip-and-remove.component";

@Component({
  selector: 'app-toolbars',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatPaginatorModule,
    BookToCComponent,
    MatSidenavModule,
    ActionsModule,
    MatChipAndRemoveComponent
],
  providers: [{ provide: MatPaginatorIntl, useClass: CustomPaginatorIntl }],
  templateUrl: './app-toolbars.component.html',
  styleUrls: ['./app-toolbars.component.scss'],

})
export class AppToolbarsComponent {
  protected app = inject(AppComponent);
	protected orator = inject(Orator);
}
