import { NgStyle } from '@angular/common';
import {
  Component,
  effect,
  inject,
  Injector,
  input,
  resource,
  Resource,
  signal,
  ViewEncapsulation,
  ResourceStatus,
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { App } from '../app';

@Component({
  selector: 'bbutton',
  imports: [MatIconModule, MatButtonModule, NgStyle, MatProgressSpinnerModule],
  host: {
    '(click)': 'onClick($event)',
  },
  template: `
    <button matMiniFab [disabled]="busy() && !g()">
      <mat-icon fontSet="material-symbols-outlined" [class.spin]="busy() && !g()">
        @if (busy() && !g()) {
          progress_activity
        } @else {
          <ng-content />
        }
      </mat-icon>
    </button>

    @if (g()) {
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
    }
    <!-- <button class="w3-button" (click)="onTOCClick(true)">TOC</button>

	<div class="w3-modal" [ngStyle]="{'display': isOpen() ? 'block' : 'none' }">
		<div class="w3-modal-content">
			<div class="w3-container">
				<h2>Table of Contents </h2><button (click)="onTOCClick(false)" class="w3-button w3-display-topright">&times;</button>
				<div>
					<table class="table">
						<thead>
							<tr>
								<th></th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<ng-container *ngFor="let tocEntry of brookeService.currentBookDetails()?.tocEntries">
								<tr (click)="goToPage(tocEntry.pageNumber)">
									<td>{{ tocEntry.pageNumber }}</td>
									<td>{{ tocEntry.name }}</td>
								</tr>
							</ng-container>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div> -->
  `,
  styles: ``,
  encapsulation: ViewEncapsulation.None,
  providers: [],
})

/**
 * just thoughts...
 * 1- convert this to a directive
 * 2- each action will have a id
 * 3- buttons can share action ids (image click vs button click)
 * 4- have the directive change the icon if there is a mat-icon
 * 5- if there's no click handler, register one & execute action on click
 * 6- 
 * .- but... 
 *      does that mean i need a global action execution thingy?
 *      if so, that'll be my man in the middle to capture the promise
 *      but i'll probably need a global registry of signals that
 *      represent the status of the action (idle/busy)
 *      
 * .- may not be able to determine if the host has a click handler
 */

export class PromiseButton {
  app = inject(App);
  injector = inject(Injector);

  protected busy = signal<boolean>(false);

  o = input<any>();
  m = input.required<() => Promise<any>>();
  g = input<boolean>(false);

  onClick(event: MouseEvent): void {
    this.busy.set(true);
    let obj = this.o() || this.app;
    let pr = this.m().call(obj);

    pr?.then(() => {
      this.busy.set(false);
    }) || this.busy.set(false);
  }
}
