import {
  Component,
  input,
  output,
  signal,
  ViewEncapsulation,
  WritableSignal,
} from '@angular/core';
import { ButtonComponent } from './button';

export declare interface BtnRadioGroupOption {
  value: string;
  icon: string;
}

export type Color = 'p' | 's' | 't';
export type OnColor = 'on' | 'container';

export type Size = 'xs' | 's' | 'm' | 'l' | 'xl';

@Component({
  selector: 'BtnRadioGroup',
  imports: [ButtonComponent],
  template: `
    @for (option of options(); track option.value) {
      <Btn
        type="fab"
        class="margin-r"
        [color]="value() === option.value ? 'p' : 't'"
        [size]="size()"
        [icon]="option.icon"
        [disabled]="disabled()"
        (click)="onClick($event, option)"
      >
      </Btn>
    }
  `,
  encapsulation: ViewEncapsulation.None,
})
export class ButtonRadioGroupComponent {
  options = input<BtnRadioGroupOption[]>();

  size = input<Size>('s');
  disabled = input<boolean>(false);
  value = input<string>();

  change = output<string>();
  click = output<MouseEvent>();

  onClick($vent: MouseEvent, option: BtnRadioGroupOption) {
    $vent.stopPropagation();
    this.click.emit($vent);
    this.change.emit(option.value);
  }
}
