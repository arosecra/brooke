import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, of, switchMap } from 'rxjs';
import { BrookeService } from '../brooke.service';

interface BookOptionsVM {
  collection?: string,
  category?: string,
  item?: string,
  leftPage?: string,
  rightPage?: string,
  pageState: BookOptionsPageState;
}

interface BookOptionsPageState {
  isActive: boolean;

}

@Component({
  selector: 'app-book-options',
  templateUrl: './book-options.component.html'
})
export class BookOptionsComponent implements OnInit {
  vm$: Observable<BookOptionsVM>;


  constructor(
    private activatedRoute: ActivatedRoute,
    private brookeService: BrookeService,
    private router: Router,
  ) { }

  ngOnInit() {

    this.vm$ = this.activatedRoute.queryParams.pipe(
      map((queryParams) => {
        let result: BookOptionsVM = {
          collection: queryParams['collection'],
          category: queryParams['category'],
          item: queryParams['item'],
          leftPage: queryParams['leftPage'],
          rightPage: queryParams['rightPage'],
          pageState: {
            isActive: false
          } as BookOptionsPageState
        }

        return result;

      })
    )
  }
}
