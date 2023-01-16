import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of, switchMap, forkJoin, map, repeat, delay } from 'rxjs';
import { Category, Collection, JobDetails } from '../brooke.model';
import { BrookeService } from '../brooke.service';
import { CollectionComponent } from '../collection/collection.component';

interface CachingVM {
  jobDetails: JobDetails;
  collection: Collection;
  queryParams: CachingQueryParams;
}

interface CachingQueryParams {
  collection: string;
  category: string;
  series: string;
  item: string;
  leftPage: string;
  rightPage: string;
}

@Component({
  selector: 'app-caching',
  templateUrl: './caching.component.html'
})
export class CachingComponent {

  vm$: Observable<CachingVM>;

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
          jobDetails: this.brookeService.cacheItem(queryParams['collection'], queryParams['item'])
        }).pipe(
          switchMap((forkJoinResult) => {

            return this.brookeService.getJobDetails(forkJoinResult.jobDetails.jobNumber).pipe(
              map((jobDetails) => {

                if (jobDetails.total > 0 && jobDetails.total === jobDetails.current) {
                  if (forkJoinResult.collection.openType === 'book') {
                    this.router.navigate(['/book'], {
                      queryParams: {
                        collection: queryParams['collection'],
                        category: queryParams['category'],
                        item: queryParams['item'],
                        leftPage: 0,
                        rightPage: 1
                      }
                    });
                  } else {
                    this.brookeService.openVideo(queryParams['collection'], queryParams['item']).subscribe(
                      () => {
                        if(queryParams['series']) {
                          this.router.navigate(['/series'], {
                            queryParams: {
                              collection: queryParams['collection'],
                              category: queryParams['category'],
                              series: queryParams['series'],
                            }
                          });
    
                        } else {
                          this.router.navigate(['/category'], {
                            queryParams: {
                              collection: queryParams['collection'],
                              category: queryParams['category']
                            }
                          });
    
                        }
                        return of({} as CachingVM);
                      }
                    )
                    

                  }
                }

                return {
                  collection: forkJoinResult.collection,
                  jobDetails: jobDetails,
                  queryParams: {
                    collection: queryParams['collection'],
                    category: queryParams['category'],
                    series: queryParams['series'],
                    item: queryParams['item'],
                    leftPage: queryParams['leftPage'],
                    rightPage: queryParams['rightPage']
                  }
                } as CachingVM;
              }),
              delay(500),
              repeat()
            )
          }

          )
        )
      }
      )
    )
  }
}
