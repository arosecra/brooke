
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, switchMap, forkJoin, map, iif } from 'rxjs';
import { BookDetails, Category, Collection } from '../brooke.model';
import { BrookeService } from '../brooke.service';

interface Page {
  elipses: boolean;
  page: string;
  current: boolean;
}

interface BookPageTurnerVM {
  ranges: Page[];
  collection: string;
  category: string;
  item: string;
  leftPage: string;
  rightPage: string;
}

const defaultVM = {
  ranges: [],
  collection: '',
  category: '',
  item: '',
  leftPage: '0',
  rightPage: '0'
}


@Component({
  selector: 'app-book-page-turner',
  templateUrl: './book-page-turner.component.html'
})
export class BookPageTurnerComponent {

  vm$: Observable<BookPageTurnerVM>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private brookeService: BrookeService,
    private router: Router,
  ) { }

  ngOnInit() {

    this.vm$ = this.activatedRoute.queryParams.pipe(
      switchMap((queryParams) => {
        if (queryParams['leftPage'] || queryParams['rightPage']) {
          return this.brookeService.getBookDetails(queryParams['collection'], queryParams['item'])
            .pipe(
              map((bookDetails: BookDetails) => {
                let leftPage = queryParams['leftPage'];

                let result: BookPageTurnerVM = {
                  ranges: [],
                  collection: queryParams['collection'],
                  category: queryParams['category'],
                  item: queryParams['item'],
                  leftPage: queryParams['leftPage'],
                  rightPage: queryParams['rightPage']
                }

                let isFirstTwoPages = leftPage < 4;
                let isLastTwoPages = leftPage > bookDetails.numberOfPages - 4;

                if (isFirstTwoPages || isLastTwoPages) {
                  //if the number is 0 to 2, or 
                  //if the number is bookDetails.numberOfPages -2 to bookDetails.numberOfPages
                  // add the first two pages
                  // add elipses
                  // add the lst two pages
                  result.ranges.push(this.createRange(0, leftPage, false))
                  result.ranges.push(this.createRange(2, leftPage, false))
                  if (leftPage === '2')
                    result.ranges.push(this.createRange(4, leftPage, false))
                  result.ranges.push(this.createRange(0, '0', true))
                  if (leftPage === '' + (bookDetails.numberOfPages - 2))
                    result.ranges.push(this.createRange(bookDetails.numberOfPages - 4, leftPage, false))
                  result.ranges.push(this.createRange(bookDetails.numberOfPages - 2, leftPage, false))
                  result.ranges.push(this.createRange(bookDetails.numberOfPages, leftPage, false))
                } else {
                  //otherwise
                  // add the first 2 pages
                  // add elipses
                  // add left -2 to left +2
                  // add elipses
                  // add last 2 pages
                  result.ranges.push(this.createRange(0, leftPage, false))
                  if (leftPage !== '2')
                    result.ranges.push(this.createRange(2, leftPage, false))
                  result.ranges.push(this.createRange(0, '0', true))
                  result.ranges.push(this.createRange(parseInt(leftPage) - 2, leftPage, false))
                  result.ranges.push(this.createRange(parseInt(leftPage), leftPage, false))
                  result.ranges.push(this.createRange(parseInt(leftPage) + 2, leftPage, false))
                  result.ranges.push(this.createRange(0, '0', true))
                  if (leftPage !== '' + (bookDetails.numberOfPages - 2))
                    result.ranges.push(this.createRange(bookDetails.numberOfPages - 2, leftPage, false))
                  result.ranges.push(this.createRange(bookDetails.numberOfPages, leftPage, false))

                }

                return result;
              })
            )
        } else {
          return of(defaultVM)
        }

      })
    )
  }

  createRange(pageNo: number, leftPage: string, elipses: boolean) {
    return {
      current: ('' + pageNo) === leftPage,
      elipses: elipses,
      page: '' + pageNo
    } as Page;
  }

  goToPage(vm: BookPageTurnerVM, page: string) {
    this.router.navigate(['/book'], {
      queryParams: {
        collection: vm.collection,
        category: vm.category,
        item: vm.item,
        leftPage: page,
        rightPage: parseFloat(page) + 1,
      }
    }
    )
  }

  toNum(no: string) {
    return parseFloat(no);
  }
}
