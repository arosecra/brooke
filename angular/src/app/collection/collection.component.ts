import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, of, switchMap } from 'rxjs';
import { Collection } from '../brooke.model';
import { BrookeService } from '../brooke.service';

@Component({
  selector: 'app-collection',
  templateUrl: './collection.component.html',
})
export class CollectionComponent implements OnInit {
  collection: string;
  vm$: Observable<Collection>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private brookeService: BrookeService,  
  ) {}
  
  ngOnInit() {

    this.vm$ = this.activatedRoute.queryParams.pipe(
      switchMap( (queryParams) => {
          return this.brookeService.getCollection(queryParams['collection']);
        }
      )
    )
  }
}
