import {
  Component,
  inject,
  input,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { AppContextProvider } from '../../../../providers/app-context-provider';
import {
  ButtonComponent,
  ChildItem,
  ChildItemRef,
  Item,
  ItemRef,
} from '../../../../shared';
import { CompleteItem } from '../../../../shared/model/CompleteItem';

@Component({
  selector: 'series-card',
  imports: [ButtonComponent],
  template: `
    @let mainActionItem =
      app.location().collection?.openType === 'book'
        ? 'file_open'
        : 'play_arrow';

    <div class="flex flex-gap flex-align-items-start">
      <table>
        <thead>
          <tr>
            <td>Name</td>
            <td>Actions</td>
          </tr>
        </thead>
        <tbody>
          @for (
            childItemRef of seriesItem().itemRef.childItems;
            track childItemRef.name
          ) {
            @let childItem =
              seriesItem().item.childItems[$index];
            <tr>
              <td>{{ childItemRef.displayName }}</td>
              <td>
                <Btn
                  type="fab"
                  icon="{{ mainActionItem }}"
                  (click)="
                    openItem(childItem, childItemRef)
                  "
                />
                <Btn
                  type="fab"
                  icon="download_for_offline"
                  (click)="downloadForOffline(childItem)"
                  [disabled]="
                    !app.settings.value()?.cacheDirectory
                  "
                />
                <Btn
                  type="fab"
                  icon="notes"
                  (click)="openItemDetails()"
                  [disabled]="
                    !app.settings.value()?.cacheDirectory
                  "
                />
              </td>
            </tr>
          }
        </tbody>
      </table>
      <img [src]="imageUrl" style="object-fit: contain;" />
    </div>
  `,
  styles: ``,
})
export class SeriesComponent implements OnInit, OnDestroy {
  app = inject(AppContextProvider).app;

  seriesItem = input.required<CompleteItem>();

  imageUrl: string;

  ngOnInit(): void {
    const thumbnail = this.seriesItem().thumbnail;
    if (thumbnail?.thumbnail) {
      this.imageUrl = URL.createObjectURL(
        thumbnail.thumbnail,
      );
    }
  }
  ngOnDestroy(): void {
    if (this.imageUrl) URL.revokeObjectURL(this.imageUrl);
  }

  downloadForOffline(item: ChildItem) {
    return this.app.cacheItem(item);
  }

  openItem(item: ChildItem, itemRef: ChildItemRef) {
    const cItem = {
      item: item as Item,
      itemRef: itemRef as ItemRef,
      thumbnail: this.seriesItem().thumbnail,
    };
    return this.app.openItem(cItem);
  }

  openItemDetails() {
    return Promise.resolve(true);
  }
}
