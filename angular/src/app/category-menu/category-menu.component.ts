import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { map, Observable, of, switchMap } from 'rxjs';
import { Collection } from '../brooke.model';
import { BrookeService } from '../brooke.service';

interface CategoryMenuVM {
  collection: Collection;
}

@Component({
  selector: 'app-category-menu',
  templateUrl: './category-menu.component.html'
})
export class CategoryMenuComponent implements OnInit {
  vm$: Observable<CategoryMenuVM>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private brookeService: BrookeService,  
  ) {}
  
  ngOnInit() {

    this.vm$ = this.activatedRoute.queryParams.pipe(
      switchMap( (queryParams) => {
          return this.brookeService.getCollection(queryParams['collection']).pipe(
            map((collection) => {
              return {
                collection: collection
              }
            })
          );
        }
      )
    )
  }
}
