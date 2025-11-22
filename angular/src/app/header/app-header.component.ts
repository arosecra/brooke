import { Component, inject, Injectable, ViewEncapsulation } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AppComponent } from '../app.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AppBreadcrumbComponent } from './app-breadcrumb.component';
import { MatPaginatorIntl, MatPaginatorModule } from '@angular/material/paginator';
import { BookToCComponent } from '../media/book-toc/book-toc.component';
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
  ],
  providers: [{ provide: MatPaginatorIntl, useClass: CustomPaginatorIntl }],
  templateUrl: './app-header.component.html',
  styles: ``,

})
export class AppHeaderComponent {
  app = inject(AppComponent);
}
