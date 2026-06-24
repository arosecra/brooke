import { Component, input, output } from '@angular/core';

@Component({
  selector: 'list',
  imports: [],
  template: `
    <div class="field">
      <select
        id="select-{{ label() }}"
        (change)="onChange($event)"
      >
        <button>
          @if (value()) {
            {{ value() }}
          }
        </button>

        <option value="">None</option>
        @for (option of options(); track option) {
          <option
            [value]="option"
            [attr.selected]="value() === option"
          >
            {{ option }}
          </option>
        }
      </select>
      <label
        class="select-label"
        for="select-{{ label() }}"
        >{{ label() }}</label
      >
    </div>
  `,
})
export class List {
  options = input<string[]>([]);
  label = input<string>();
  value = input<string>();

  change = output<string>();

  onChange(event: Event) {
    event.stopPropagation();
    const element = event.target as HTMLSelectElement;
    const newValue = element.value;
    console.log(
      'Selected value directly from DOM:',
      newValue,
    );
    this.change.emit(newValue);
  }
}
