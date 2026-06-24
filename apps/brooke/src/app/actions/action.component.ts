import { Component, inject, input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AppContextProvider } from '../../providers/app-context-provider';

@Component({
  selector: 'action',
  imports: [
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
  ],
  host: {
    '(click)': 'onClick()',
  },
  template: `
    <div style="position: relative">
      @let imgVal = img();
      @if (imgVal) {
        <img [src]="imgVal" />
      }
      @if (tonal()) {
        <button
          matButton="tonal"
          [disabled]="app.busy() || !!disabled()"
          [attr.title]="title()"
        >
          {{ tonal() }}
        </button>
      } @else {
        <button
          matMiniFab
          [disabled]="app.busy() || !!disabled()"
          [class.button-overlay]="!!imgVal"
          [attr.title]="title()"
        >
          <div [class.button-overlay-circle]="!!imgVal">
            <mat-icon
              fontSet="material-symbols-outlined"
              [class.spin]="app.busy()"
              [class.button-overlay-icon]="!!imgVal"
            >
              @if (app.busy()) {
                progress_activity
              } @else {
                <ng-content />
              }
            </mat-icon>
          </div>
        </button>
      }
    </div>

    <!-- @if (g()) {
  	  <div
  	    class="overlay"
  	    [ngStyle]="{ display: busy() ? 'block' : 'none' }"
  	    (click)="$event.stopPropagation()"
  	  >
  	    <div class="overlay-content">
  	      <div class="flex flex-align-center flex-justify-center flex-column">
  	        <div><h2>Busy</h2></div>
  	        <div>Please Wait</div>
  	        <mat-spinner></mat-spinner>
  	      </div>
  	    </div>
  	  </div>
  	} -->
  `,
  styles: `
    .button-overlay {
      position: absolute;
      top: calc(50% - 32px);
      left: calc(50% - 32px);
      background-color: transparent;
    }

    .button-overlay-circle {
      background-color: rgba(128, 128, 128, 0.5);
      height: 64px;
      width: 64px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .button-overlay-icon {
      color: white;
      height: 32px;
      width: 32px;
      line-height: 32px;
    }
  `,

  providers: [],
})
export class ActionComponent {
  app = inject(AppContextProvider).app;

  o = input<any>();
  m = input.required<(...args: any[]) => Promise<any>>();
  p = input<any[]>();
  img = input<string>();
  tonal = input<string>();

  disabled = input<boolean>(false);
  title = input<string>('');

  onClick(): void {
    this.app.busy.set(true);
    let obj = this.o() ?? this.app;
    let args = this.p();
    let pr = args
      ? this.m().apply(obj, args)
      : this.m().apply(obj);

    pr?.then(() => {
      this.app.busy.set(false);
    }) ?? this.app.busy.set(false);
  }
}
