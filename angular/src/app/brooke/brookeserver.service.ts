import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { BookDetails, CacheManifest, Category, Collection, Item, JobDetails, MissingItem } from './brooke.model';

@Injectable()
export class BrookeServerService {

	private http: HttpClient = inject(HttpClient);

  constructor() { }

  getCollections(): Observable<Collection[]> {
		if((window as any).api?.getCollections) {
			return of((window as any).api.getCollections);
		} else {
			return this.http.get<Collection[]>(
				'/rest/collection'
			);
		}
  }

  getCollection(name: string): Observable<Collection> {
    return this.http.get<Collection>(
      `/rest/collection/${name}`
    );
  }

  getCategory(collectionName: string, categoryName: string): Observable<Category> {
    return this.http.get<Category>(
      `/rest/category/${collectionName}/${categoryName}`
    );
  }

  getItem(collectionName: string, categoryName: string, item: string): Observable<Item> {
    return this.http.get<Item>(
      `/rest/item/${collectionName}/${categoryName}/${item}`
    );
  }

  getSeries(collectionName: string, categoryName: string, seriesName: string): Observable<Item> {
    return this.http.get<Item>(
      `/rest/series/${collectionName}/${categoryName}/${seriesName}`
    );
  }

  cacheItem(collectionName: string, itemName: string): Observable<JobDetails> {
    return this.http.get<JobDetails>(
      `/rest/cache/${collectionName}/${itemName}`
    );
  }

	copyToBooxTablet(collectionName: string, itemName: string): Observable<JobDetails> {
		return this.http.get<JobDetails>(
			`/rest/copy-to-boox-tablet/${collectionName}/${itemName}`
		)
	}

	copyToDevice(collectionName: string, itemName: string, deviceName: string): Observable<JobDetails> {
		return this.http.get<JobDetails>(
			`/rest/copy-to/${deviceName}/${collectionName}/${itemName}`
		)
	}

  synchronize(): Observable<JobDetails> {
    return this.http.get<JobDetails>(
      `/rest/administration/sync`
    );
  }

  reload(): Observable<void> {
    return this.http.get<void>(
      `/rest/administration/reload`
    );
  }

  openVideo(collectionName: string, itemName: string): Observable<JobDetails> {
    return this.http.get<JobDetails>(
      `/rest/video/${collectionName}/${itemName}`
    );
  }

  getBookDetails(collectionName: string, itemName: string): Observable<BookDetails> {
    return this.http.get<BookDetails>(
      `/rest/book-details/${collectionName}/${itemName}`
    );
  }

  getJobDetails(jobNumber: bigint): Observable<JobDetails> {
    return this.http.get<JobDetails>(
      `/rest/job-details/${jobNumber}`
    );
  }

  getAllJobDetails(): Observable<JobDetails[]> {
    return this.http.get<JobDetails[]>(
      `/rest/job-details`
    );
  }

	getCachedManifest(): Observable<CacheManifest> {
		return this.http.get<CacheManifest>(`/rest/cache`);
	}

	getMissingItems(): Observable<MissingItem[]> {
		return this.http.get<MissingItem[]>(
      `/rest/administration/missing-item`
    );
	}
}