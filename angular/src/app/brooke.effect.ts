import { Injectable } from "@angular/core";
import { createEffect } from "@ngrx/effects";
import { Actions, ofType } from "@ngrx/effects";
import { Action } from "@ngrx/store";
import { catchError, map, Observable, of, switchMap } from "rxjs";
import { getCollections, openCollection } from "./brooke.action";
import { BrookeService } from "./brooke.service";



@Injectable()
export class BrookeEffect {

	getCollections$ = createEffect(() =>
        this.actions$.pipe(
          ofType(getCollections),
          switchMap(action => {
            return this.brookeService.getCollections().pipe(
							map((collections) => {
								return {
									type: '[Brooke] Get Collections',
									collections: collections
								}
							})
						)
          }),
          catchError(error => of({ 
            type: '[Brooke] Get Collections', message: error 
          }))
        )
      );

	constructor(
		private actions$: Actions, 
		private brookeService: BrookeService
	) {
	}
}