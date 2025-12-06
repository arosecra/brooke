import { Component, inject } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { ActionsModule } from '../../actions/actions.module';
import { AppComponent } from '../../app.component';
import { ItemCardComponent } from '../cards/item-card/item-card.component';
import { SeriesComponent } from '../cards/series-card/series-card.component';

@Component({
  selector: 'collection-browser',
  imports: [
    MatBadgeModule,
    MatButtonModule,
    MatChipsModule,
    MatIconModule,
    ActionsModule,
    ItemCardComponent,
    SeriesComponent,
  ],
  templateUrl: './collection-browser.component.html',
  styleUrls: ['./collection-browser.component.scss'],
})
export class CollectionBrowserComponent {
  app = inject(AppComponent);
}
