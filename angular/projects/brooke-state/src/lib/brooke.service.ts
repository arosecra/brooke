import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import {
  catchError,
  delay,
  retry,
  repeat,
  take,
  filter,
  tap,
} from 'rxjs/operators';
import {
  BookDetails,
  BrookeLocation,
  CacheManifest,
  Category,
  Collection,
  Item,
  JobDetails,
  MissingItem,
  Page,
} from 'brooke-domain';
import { BrookeServerService } from 'brooke-server';
import { toSignal } from '@angular/core/rxjs-interop';

@Injectable()
export class BrookeService {
  constructor(private brookeServerService: BrookeServerService) {}

	widgets = {
		asideMenuExpanded: signal<boolean>(true)
	}

  currentCollection = signal<Collection | undefined>(undefined);
  currentCategory = signal<Category | undefined>(undefined);
  currentSeries = signal<Item | undefined>(undefined);
  currentItem = signal<Item | undefined>(undefined);
  currentLeftPage = signal<string | undefined>(undefined);
  currentRightPage = signal<string | undefined>(undefined);
  currentBookDetails = signal<BookDetails | undefined>(undefined);

  lastItem = signal<Item | undefined>(undefined);

  currentJob = signal<JobDetails | undefined>(undefined);

  collections = toSignal(this.brookeServerService.getCollections());
  cachedManifest = toSignal(this.brookeServerService.getCachedManifest());

  currentLocation = computed<BrookeLocation>(() => {
    return {
      collection: this.currentCollection(),
      category: this.currentCategory(),
      series: this.currentSeries(),
      item: this.currentItem(),
      jobDetails: this.currentJob(),
      leftPage: this.currentLeftPage(),
      rightPage: this.currentRightPage(),
    } as BrookeLocation;
  });

  pages = computed<Page[]>(() => {
    let result: Page[] = [];

    if (this.currentBookDetails()) {
      let leftPage = this.currentLeftPage() ?? '0';
      let numberOfPages = this.currentBookDetails()?.numberOfPages ?? 4;
      let isFirstTwoPages = Number(leftPage) < 4;
      let isLastTwoPages = Number(leftPage) > numberOfPages - 4;

      if (isFirstTwoPages || isLastTwoPages) {
        //if the number is 0 to 2, or
        //if the number is bookDetails.numberOfPages -2 to bookDetails.numberOfPages
        // add the first two pages
        // add elipses
        // add the lst two pages
        result.push(this.createRange(0, leftPage, false));
        result.push(this.createRange(2, leftPage, false));
        if (leftPage === '2') result.push(this.createRange(4, leftPage, false));
        result.push(this.createRange(0, '0', true));
        if (leftPage === '' + (numberOfPages - 2))
          result.push(this.createRange(numberOfPages - 4, leftPage, false));
        result.push(this.createRange(numberOfPages - 2, leftPage, false));
        result.push(this.createRange(numberOfPages, leftPage, false));
      } else {
        //otherwise
        // add the first 2 pages
        // add elipses
        // add left -2 to left +2
        // add elipses
        // add last 2 pages
        result.push(this.createRange(0, leftPage, false));
        if (leftPage !== '2') result.push(this.createRange(2, leftPage, false));
        result.push(this.createRange(0, '0', true));
        result.push(this.createRange(parseInt(leftPage) - 2, leftPage, false));
        result.push(this.createRange(parseInt(leftPage), leftPage, false));
        result.push(this.createRange(parseInt(leftPage) + 2, leftPage, false));
        result.push(this.createRange(0, '0', true));
        if (leftPage !== '' + (numberOfPages - 2))
          result.push(this.createRange(numberOfPages - 2, leftPage, false));
        result.push(this.createRange(numberOfPages, leftPage, false));
      }
    }

    return result;
  });

  private createRange(pageNo: number, leftPage: string, elipses: boolean) {
    return {
      current: '' + pageNo === leftPage,
      elipses: elipses,
      page: '' + pageNo,
    } as Page;
  }

	
	copyToTablet(item: Item) {
    this.brookeServerService
      .copyToTablet(this.currentCollection()?.name ?? 'undefined', item.name)
      .subscribe((result: JobDetails) => {
        let sub = this.brookeServerService
          .getJobDetails(result.jobNumber)
          .pipe(
            repeat({ delay: 1000 }),
            tap((jobDetails) => {
              // if(jobDetails.current === jobDetails.total && jobDetails.total > 0)
              // 	sub.unsubscribe()
              this.currentJob.update(() => jobDetails);
            }),
            filter((res) => {
              console.log(res.current, res.total);
              return res.current === res.total && res.total > 0;
            })
            // take(1)
          )
          .subscribe((done) => {
            this.currentJob.update(() => undefined);
            console.log('done', done.current, done.total);
            sub.unsubscribe();
          });

        this.currentJob.update(() => result);
      });
  }

  openItem(item: Item) {
    if (this.currentCollection()?.openType === 'video' && item.series) {
      this.currentSeries.update(() => item);
			this.currentItem.update(() => undefined);
    } else {
      //check if this is a cached file
      let isCached = this.cachedManifest()?.files.some((value) => {
        return (
          value.collectionName === this.currentCollection()?.name &&
          value.itemName === item.name
        );
      });

      if (!isCached) {
        this.cacheItem(item);
      } else {
        this.displayItem(item);
      }
    }
  }

  displayItem(item: any) {
    if (this.currentCollection()?.openType === 'book') {
      this.displayBookItem(item);
    } else {
			this.displayVideoItem(item);
    }
    this.lastItem.update(() => item);
  }

  private displayBookItem(item: any) {
    this.currentItem.update(() => item);

    if (this.lastItem()?.name !== item.name) {
			this.currentBookDetails.update(() => undefined);
      this.currentLeftPage.update(() => `0`);
      this.currentRightPage.update(() => `1`);
    }

    this.brookeServerService
      .getBookDetails(this.currentCollection()?.name ?? 'undefined', item.name)
      .subscribe((bookDetails) => {
        this.currentBookDetails.update(() => bookDetails);
      });
  }

	private displayVideoItem(item: any) {
		this.brookeServerService.openVideo(this.currentCollection()?.name ?? 'undefined', item.name)
		.subscribe(() => {
			this.currentItem.update(() => item);

		})
	}

  cacheItem(item: Item) {
    this.brookeServerService
      .cacheItem(this.currentCollection()?.name ?? 'undefined', item.name)
      .subscribe((result: JobDetails) => {
        let sub = this.brookeServerService
          .getJobDetails(result.jobNumber)
          .pipe(
            repeat({ delay: 1000 }),
            tap((jobDetails) => {
              // if(jobDetails.current === jobDetails.total && jobDetails.total > 0)
              // 	sub.unsubscribe()
              this.currentJob.update(() => jobDetails);
            }),
            filter((res) => {
              console.log(res.current, res.total);
              return res.current === res.total && res.total > 0;
            })
            // take(1)
          )
          .subscribe((done) => {
            this.currentJob.update(() => undefined);
            console.log('done', done.current, done.total);
            this.displayItem(item);
            sub.unsubscribe();
          });

        this.currentJob.update(() => result);
      });
  }
}
