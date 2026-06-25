import {
  Component,
  inject,
  input,
  output,
} from '@angular/core';
import { AppContextProvider } from '../../providers/app-context-provider';
import { Item } from '../model/Item';
import { ItemRef } from '../model/ItemRef';
import { Thumbnail } from '../model/Thumbnail';

@Component({
  selector: 'figure-button',
  imports: [],
  template: `
    <figure class="underlay-figure">
      <img [src]="img()" (click)="onClick($event)" />
      <button
        type="button"
        class="overlay-button"
        (click)="onClick($event)"
      >
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
      padding: 0;
      margin: 0;
      cursor: pointer;
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
      cursor: pointer;
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
  img = input<string>();
  icon = input<string>();
  click = output();

  onClick($event: any) {
    $event?.stopPropagation();
    this.click.emit();
  }
}
