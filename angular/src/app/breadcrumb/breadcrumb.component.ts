import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { map, Observable, of, switchMap } from 'rxjs';
import { Collection } from '../brooke.model';
import { BrookeService } from '../brooke.service';

interface BreadcrumbVM {
  collection: string;
  category: string;
  series: string;
  item: string;
}

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html'
})
export class BreadcrumbComponent implements OnInit {
  vm$: Observable<BreadcrumbVM>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
  ) {
  }
  
  ngOnInit() {

    this.vm$ = this.activatedRoute.queryParams.pipe(
      map( (queryParams) => {
          return {
            collection: queryParams['collection'],
            category: queryParams['category']
          } as BreadcrumbVM;
        }
      )
    )
  }

  openCollection(vm: BreadcrumbVM) {
    this.router.navigate(['/collection'], {
      queryParams: {
        collection: vm.collection
      }
    })
  }

  openCategory(vm: BreadcrumbVM) {
    this.router.navigate(['/category'], {
      queryParams: {
        collection: vm.collection,
        category: vm.category
      }
    })
  }
}
