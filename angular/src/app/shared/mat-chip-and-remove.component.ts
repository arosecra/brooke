import { Component, input, output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { ItemNamePipe } from './item-name.pipe';

@Component({
  selector: 'mat-chip-and-remove',
  imports: [MatIconModule, MatChipsModule, ItemNamePipe],
  template: `
    <mat-chip (removed)="remove()">
      {{ label() | itemName }}
      <button matChipRemove>
        <mat-icon class="icon-fill icon-wght-700" fontSet="material-symbols-outlined">cancel</mat-icon>
      </button>
    </mat-chip>
  `,
  styles: ``,

})
export class MatChipAndRemoveComponent {
  label = input<string | undefined>();
  removed = output<boolean>();

  remove() {
    this.removed.emit(true);
  }
}
