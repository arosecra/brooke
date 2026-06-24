import { Component, inject, input } from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';
import { Item } from '../model/Item';
import { ItemRef } from '../model/ItemRef';
import { Thumbnail } from '../model/Thumbnail';

@Component({
  selector: 'figure-button',
  imports: [],
  template: `
    <figure class="underlay-figure">
      <img [src]="imageUrl" />
      <button type="button" class="overlay-button">
        <span class="icon icon-font overlay-icon">
          @if (app.busy()) {
            progress_activity
          } @else {
            {{ icon() }}
          }
        </span>
      </button>
    </figure>
  `,
  styles: `
    .underlay-figure {
      position: relative;
      margin: 24px;
      padding: 0;
    }

    .overlay-button {
      position: absolute;
      left: calc(50% - 32px);
      top: calc(50% - 32px);
      background-color: rgba(128, 128, 128, 0.5);
      height: 64px;
      width: 64px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .overlay-icon {
      color: white;
      height: 32px;
      width: 32px;
      line-height: 32px;
      font-size: 32px;
    }
  `,
})
export class FigureButton {
  app = inject(AppContextProvider).app;
  itemRef = input.required<ItemRef>();
  item = input.required<Item>();
  thumbnail = input<Thumbnail>();
  icon = input<string>();

  imageUrl!: string;

  ngOnInit(): void {
    const item = this.thumbnail();
    if (item) {
      this.imageUrl = URL.createObjectURL(item.thumbnail);
    }
  }
  ngOnDestroy(): void {
    if (this.imageUrl) URL.revokeObjectURL(this.imageUrl);
  }
}
