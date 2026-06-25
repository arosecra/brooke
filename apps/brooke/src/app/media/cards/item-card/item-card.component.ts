import {
  Component,
  inject,
  input,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { AppContextProvider } from '../../../../providers/app-context-provider';
import { ButtonComponent } from '../../../../shared';
import { CompleteItem } from '../../../../shared/model/CompleteItem';
import { FigureButton } from '../../../../shared/widgets/figure-button';
import { Card } from '../card/card';
import { CardActions } from '../card/card-actions';
import { CardBody } from '../card/card-body';
import { CardHeader } from '../card/card-header';

@Component({
  selector: 'item-card',
  imports: [
    Card,
    CardHeader,
    CardBody,
    CardActions,
    ButtonComponent,
    FigureButton,
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
    const item = this.completeItem();
    if (item) {
      this.imageUrl = URL.createObjectURL(
        item.thumbnail.thumbnail,
      );
    }
  }
  ngOnDestroy(): void {
    if (this.imageUrl) URL.revokeObjectURL(this.imageUrl);
  }

  downloadForOffline() {
    return this.app.cacheItem(this.completeItem().item);
  }

  openItem() {
    return this.app.openItem(this.completeItem());
  }

  openItemThumbnails() {
    return this.app.openItemThumbnails(this.completeItem());
  }

  openItemMarkdown() {
    return this.app.openItemMarkdown(this.completeItem());
  }

  openItemDetails() {
    return Promise.resolve(true);
  }
}
