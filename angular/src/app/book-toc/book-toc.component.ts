import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, of, switchMap } from 'rxjs';
import { BookDetails, Collection } from '../brooke.model';
import { BrookeService } from '../brooke.service';

interface BookTOCVM {
  collection?: string,
  category?: string,
  item?: string,
  leftPage?: string,
  rightPage?: string,
  bookDetails?: BookDetails,
  pageState: BookTOCPageState
}

interface BookTOCPageState {
  isActive: boolean;

}

const defaultVM: BookTOCVM = {
  pageState: {
    isActive: false
  }
}

@Component({
  selector: 'app-book-toc',
  templateUrl: './book-toc.component.html'
})
export class BookTocComponent {
  vm$: Observable<BookTOCVM>;

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
                let result: BookTOCVM = {
                  collection: queryParams['collection'],
                  category: queryParams['category'],
                  item: queryParams['item'],
                  leftPage: queryParams['leftPage'],
                  rightPage: queryParams['rightPage'],
                  bookDetails: bookDetails,
                  pageState: {
                    isActive: false
                  }
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

  isTocPresent(vm: BookTOCVM): any {
    return vm.bookDetails &&
      vm.bookDetails.tocEntries &&
      vm.bookDetails.tocEntries.length > 0
  }

  goToPage(vm: BookTOCVM, page: string) {
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
}
