import { Component, input, output, signal, ViewEncapsulation, WritableSignal } from '@angular/core';



export type ButtonType = 'fab' | 'efab' | 'icon' | 'filled' | 'overlay';

export type Color = 'p' | 's' | 't';
export type OnColor = 'on' | 'container'

export type Size = 'xs' | 's' | 'm' | 'l' | 'xl';

@Component({
  selector: 'Btn',
  imports: [],
  template: `
    <button 
      type="button" 
      [class.primary]="color() === 'p'"
      [class.secondary]="color() === 's'"
      [class.tertiary]="color() === 't'"
      [class.on]="oncolor() === 'on'"
      [class.on-container]="oncolor() === 'container'"
      [class.fab]="type() === 'fab'"
      [class.extended-fab]="type() === 'efab'"
      [class.filled]="type() === 'filled'"
      [class.extra-small]="size() === 'xs'"
      [class.small]="size() === 's' || !size()"
      [class.medium]="size() === 'm'"
      [class.large]="size() === 'l'"
      [class.extra-large]="size() === 'xl'"
      class="btn margin elv lvl3"
      (click)="onClick()"
      [disabled]="disabled()"
    >
      @if(icon()) {
        <span class="icon icon-font">{{ icon() }}</span>
      }
      @if(label() && type() !== 'fab') {
        <span class="label">{{ label() }}</span>
      }
    </button>
  `,
  encapsulation: ViewEncapsulation.None,
})
export class ButtonComponent {
  search = signal<string>('');
  type = input<ButtonType>();
  icon = input<string>();
  label = input<string>();
  color = input<Color>();
  oncolor = input<OnColor>('on');
  size = input<Size>('s');
  disabled = input<boolean>(false);
  action = input<Function>();
  busy = input<WritableSignal<boolean>>();

  click = output();

  onClick() {
    const act = this.action();
    if (act) {
      this.busy()?.set(true);
      const res = act();
      if (res instanceof Promise) {
        res.then(() => {
          this.busy()?.set(false);
        });
      } else {
        this.busy()?.set(false);
      }
    } else {
      this.click.emit();
    }
  }
}
