import { Component, inject, ViewEncapsulation } from '@angular/core';
import { AppComponent } from '../app.component';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatChipAndRemoveComponent } from '../shared/mat-chip-and-remove.component';

@Component({
  selector: 'app-breadcrumb',
  imports: [MatIconModule, MatButtonModule, MatChipsModule, MatChipAndRemoveComponent],
  templateUrl: './app-breadcrumb.component.html',
  styleUrls: ['./app-breadcrumb.component.scss'],

})
export class AppBreadcrumbComponent {
  app = inject(AppComponent);
}
