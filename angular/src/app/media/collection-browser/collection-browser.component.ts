import { Component, inject } from '@angular/core';
import { AppComponent } from '../../app.component';
import { ItemCardComponent } from '../cards/item-card/item-card.component';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { SeriesComponent } from '../cards/series-card/series-card.component';
import { ActionComponent } from '../../actions/action.component';
import { ActionsModule } from '../../actions/actions.module';
import { Category } from '../../model/category';
import { MatBadgeModule } from '@angular/material/badge';

@Component({
  selector: 'collection-browser',
  imports: [
    ItemCardComponent,
    MatButtonModule,
    MatChipsModule,
    MatIconModule,
    MatButtonModule,
    SeriesComponent,
    MatBadgeModule,
    ActionsModule,
  ],
  templateUrl: './collection-browser.component.html',
  styleUrls: ['./collection-browser.component.scss'],
})
export class CollectionBrowserComponent {
  app = inject(AppComponent);
}
