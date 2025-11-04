import { NgStyle } from '@angular/common';
import {
	Component,
	input,
	signal,
	ViewEncapsulation
} from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'mini-fab-async',
  imports: [MatIconModule, MatButtonModule, NgStyle, MatProgressSpinnerModule],
  host: {
    '(click)': 'onClick($event)',
  },
  template: `
    <button matMiniFab [disabled]="busy() && !globalBusy()">
      <mat-icon fontSet="material-symbols-outlined" [class.spin]="busy() && !globalBusy()">
        @if (busy() && !globalBusy()) {
          progress_activity
        } @else {
          <ng-content />
        }
      </mat-icon>
    </button>

    @if (globalBusy()) {
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
//Different wait modes
//////////////////////
// function returns a promise
// function reloads / reloads a resource
// 
export class MiniFabAsync {
  busy = signal<boolean>(false);

  o = input.required<any>();
  m = input.required<() => ( Promise<any> | void )>();

  globalBusy = input<boolean>(false);
  promise = input<boolean>(true);

	onClick(event: MouseEvent): void {
    if (this.promise()) {
      this.busy.set(true);
      const pr = this.m().call(this.o());
			if(pr) {
				pr.then(() => {
					this.busy.set(false);
				});
			}
    } else {
      this.m().call(this.o());
    }
  }
}
