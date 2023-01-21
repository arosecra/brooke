
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of, switchMap, forkJoin, map } from 'rxjs';
import { Category, Collection } from '../brooke.model';
import { BrookeService } from '../brooke.service';

interface AppCategoryVM {
  collection: Collection;
  category: Category;
  pageState: AppCategoryPageState;
}

interface AppCategoryPageState {
  columns: number;
  rowHeight: string;
}

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
})
export class CategoryComponent {

  vm$: Observable<AppCategoryVM>;

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
          category: this.brookeService.getCategory(queryParams['collection'], queryParams['category'])
        }).pipe(
          map((forkJoinResult: any) => {
            let pageState: AppCategoryPageState = {
              columns: 7,
              rowHeight: '350px'
            }

            let result: AppCategoryVM = {
              collection: forkJoinResult.collection,
              category: forkJoinResult.category,
              pageState: pageState,
            }

            if(result.collection.openType === 'video') {
              result.pageState.columns = 3;
              result.pageState.rowHeight = '300px';
            }

            return result;
          })
        )
      }
      )
    )
  }
}
