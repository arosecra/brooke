import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, Observable, switchMap } from 'rxjs';
import { Category, Collection, Item } from '../brooke.model';
import { BrookeService } from '../brooke.service';
import { map } from 'rxjs/operators';

interface AppSeriesVM {
  collection: Collection;
  category: Category;
  series: Item;
  pageState: AppCategoryPageState;
}

interface AppCategoryPageState {
  columns: number;
  rowHeight: string;
}

@Component({
  selector: 'app-series',
  templateUrl: './series.component.html',
})
export class SeriesComponent {

  vm$: Observable<AppSeriesVM>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private brookeService: BrookeService,
    private router: Router,
  ) {
  }

  ngOnInit() {

    this.vm$ = this.activatedRoute.queryParams.pipe(
      switchMap((queryParams) => {
        return forkJoin({
          collection: this.brookeService.getCollection(queryParams['collection']),
          category: this.brookeService.getCategory(queryParams['collection'], queryParams['category']),
          series: this.brookeService.getSeries(queryParams['collection'], queryParams['category'], queryParams['series'])
        }).pipe(
          map((forkJoinResult: any) => {
            let pageState: AppCategoryPageState = {
              columns: 7,
              rowHeight: '350px'
            }

            let result: AppSeriesVM = {
              collection: forkJoinResult.collection,
              category: forkJoinResult.category,
              series: forkJoinResult.series,
              pageState: pageState,
            }

            if (result.collection.openType === 'video') {
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

  openItem(vm: AppSeriesVM, childItem: Item) {
    this.router.navigate(['/cache'], {queryParams: {
      collection: vm.collection.name,
      category: vm.category.name,
      series: vm.series.name,
      item: childItem.name
    }});
  }

}
