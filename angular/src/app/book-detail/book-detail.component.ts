import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, switchMap, forkJoin, map, repeat, delay } from 'rxjs';
import { BookDetails, Category, Collection, Item, JobDetails } from '../brooke.model';
import { BrookeService } from '../brooke.service';
import { CollectionComponent } from '../collection/collection.component';

interface BookDetailVM {
  collection: Collection;
  category: Category;
  item: Item;
	bookDetails: BookDetails;
}

@Component({
  selector: 'app-book-detail',
  templateUrl: './book-detail.component.html'
})
export class BookDetailComponent implements OnInit {

	vm$: Observable<BookDetailVM>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private brookeService: BrookeService,
    private router: Router,
  ) {
  }

	ngOnInit(): void {

    this.vm$ = this.activatedRoute.queryParams.pipe(
      switchMap((queryParams) => {
        return forkJoin({
          collection: this.brookeService.getCollection(queryParams['collection']),
          category: this.brookeService.getCategory(queryParams['collection'], queryParams['category']),
          item: this.brookeService.getItem(queryParams['collection'], queryParams['category'], queryParams['item']),
					bookDetails: this.brookeService.getBookDetails(queryParams['collection'], queryParams['item'])
        }).pipe(
          map((forkJoinResult: any) => {

            let result: BookDetailVM = {
              collection: forkJoinResult.collection,
              category: forkJoinResult.category,
              item: forkJoinResult.item,
							bookDetails: forkJoinResult.bookDetails
            }

            return result;
          })
        )
      })
    )
	}

	

  goToPage(vm: BookDetailVM, page: string) {
    this.router.navigate(['/cache'], {
      queryParams: {
        collection: vm.collection.name,
        category: vm.category.name,
        item: vm.item.name,
        leftPage: page,
        rightPage: parseFloat(page) + 1,
      }
    }
    )
  }
}
