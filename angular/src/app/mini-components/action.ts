import { NgStyle } from '@angular/common';
import {
	Component,
	inject,
	input,
	signal,
	ViewEncapsulation
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { App } from '../app';

@Component({
  selector: 'action',
  imports: [MatIconModule, MatButtonModule, NgStyle, MatProgressSpinnerModule],
  host: {
    '(click)': 'onClick()',
  },
  template: `
		@let imgVal = img();
		@if(imgVal) {
			<img [src]="imgVal" >
		}
		<button 
			matMiniFab
			[disabled]="app.widgets().busy()" 
			[class.button-overlay]="!!imgVal"
		>
			<div [class.button-overlay-circle]="!!imgVal">
				<mat-icon 
					fontSet="material-symbols-outlined" 
					[class.spin]="app.widgets().busy()"
					[class.button-overlay-icon]="!!imgVal" 
				>
					@if (app.widgets().busy()) {
						progress_activity
					} @else {
						<ng-content />
					}
				</mat-icon>
			</div>
    </button>

  	<!-- @if (g()) {
  	  <div
  	    class="overlay"
  	    [ngStyle]="{ display: busy() ? 'block' : 'none' }"
  	    (click)="$event.stopPropagation()"
  	  >
  	    <div class="overlay-content">
  	      <div class="flex flex-align-center flex-justify-center column-flex">
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
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
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
	// position: relative;
  // top: calc(50% - 32px);
  // left: calc(50% - 32px);
}
	
	`,
  encapsulation: ViewEncapsulation.None,
  providers: [],
})
export class Action {
  app = inject(App);

  protected static busy = signal<boolean>(false);

  o = input<any>();
  m = input.required<() => Promise<any>>();

  img = input<string>();

  onClick(): void {
    this.app.widgets().busy.set(true);
    let obj = this.o() ?? this.app;
    let pr = this.m().call(obj);

    pr?.then(() => {
      this.app.widgets().busy.set(false);
    }) ?? this.app.widgets().busy.set(false);
  }
}
