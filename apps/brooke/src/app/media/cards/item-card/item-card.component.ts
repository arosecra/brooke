import {
  Component,
  inject,
  input,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { AppContextProvider } from '../../../../providers/app-context-provider';
import { ActionComponent } from '../../../actions/action.component';
import { CompleteItem } from '../../../../shared/model/CompleteItem';
import { Card } from '../card/card';
import { CardHeader } from '../card/card-header';
import { CardBody } from '../card/card-body';
import { CardActions } from '../card/card-actions';

@Component({
  selector: 'item-card',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    ActionComponent,
    MatBadgeModule,
    Card,
    CardHeader,
    CardBody,
    CardActions,
  ],
  templateUrl: './item-card.component.html',
  styleUrls: ['./item-card.component.scss'],
})
export class ItemCardComponent
  implements OnInit, OnDestroy
{
  app = inject(AppContextProvider).app;
  completeItem = input.required<CompleteItem>();

  imageUrl: string;

  ngOnInit(): void {
    const item = this.completeItem().thumbnail;
    if (item) {
      this.imageUrl = URL.createObjectURL(item.thumbnail);
    }
  }
  ngOnDestroy(): void {
    if (this.imageUrl) URL.revokeObjectURL(this.imageUrl);
  }

  downloadForOffline() {
    return this.app.cacheItem(this.completeItem().item);
  }

  openItem() {
    return this.app.openItem(this.completeItem().item);
  }

  openItemThumbnails() {
    return this.app.openItemThumbnails(
      this.completeItem().item,
    );
  }

  openItemMarkdown() {
    return this.app.openItemMarkdown(
      this.completeItem().item,
    );
  }

  openItemDetails() {
    return Promise.resolve(true);
  }
}
