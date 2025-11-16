import { Component, input, output, ViewEncapsulation } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { ItemNamePipe } from './itemName.pipe';

@Component({
  selector: 'mat-chip-and-remove',
  imports: [MatIconModule, MatChipsModule, ItemNamePipe],
  template: `
    <mat-chip (removed)="remove()">
      {{ label() | itemName }}
      <button matChipRemove>
        <mat-icon>cancel</mat-icon>
      </button>
    </mat-chip>
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
})
export class MatChipAndRemoveComponent {
  label = input<string | undefined>();
  removed = output<boolean>();

  remove() {
    this.removed.emit(true);
  }
}
