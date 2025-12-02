import { Component, input, output } from '@angular/core';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'mat-chip-and-remove',
  imports: [MatIconModule, MatChipsModule],
  template: `
    <mat-chip (removed)="remove()">
      {{ label() }}
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
