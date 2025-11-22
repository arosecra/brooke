import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorIntl, MatPaginatorModule } from '@angular/material/paginator';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AppComponent } from '../app.component';
import { Orator } from '../audio/orator';
import { BookToCComponent } from '../media/book-toc/book-toc.component';
import { ActionComponent } from '../shared/action.component';
import { AppBreadcrumbComponent } from './app-breadcrumb.component';
import { CustomPaginatorIntl } from './custom-paginator-intl.service';

@Component({
  selector: 'app-header',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatPaginatorModule,
    BookToCComponent,
    AppBreadcrumbComponent,
		ActionComponent
  ],
  providers: [{ provide: MatPaginatorIntl, useClass: CustomPaginatorIntl }],
  templateUrl: './app-header.component.html',
  styleUrls: ['./app-header.component.scss'],

})
export class AppHeaderComponent {
  protected app = inject(AppComponent);
	protected orator = inject(Orator);
}
