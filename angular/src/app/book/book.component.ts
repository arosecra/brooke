import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { forkJoin, Observable, switchMap } from 'rxjs';
import { Category, Collection, Item } from '../brooke.model';
import { BrookeService } from '../brooke.service';
import { map } from 'rxjs/operators';

interface AppBookVM {
  collection: Collection;
  category: Category;
  item: Item;
  leftPage: number;
  rightPage: number;
  pageState: AppBookPageState;
}

interface AppBookPageState {
 
}

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
})
export class BookComponent {

  vm$: Observable<AppBookVM>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private brookeService: BrookeService,
  ) {
  }

  ngOnInit() {

    this.vm$ = this.activatedRoute.queryParams.pipe(
      switchMap((queryParams) => {
        return forkJoin({
          collection: this.brookeService.getCollection(queryParams['collection']),
          category: this.brookeService.getCategory(queryParams['collection'], queryParams['category']),
          item: queryParams['item'],
        }).pipe(
          map((forkJoinResult: any) => {
            let pageState: AppBookPageState = {
            }

            let result: AppBookVM = {
              collection: forkJoinResult.collection,
              category: forkJoinResult.category,
              item: queryParams['item'],
              leftPage: queryParams['leftPage'],
              rightPage: queryParams['rightPage'],
              pageState: pageState,
            }

            return result;
          })
        )
      }
      )
    )
  }
}
