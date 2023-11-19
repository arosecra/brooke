import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {BookDetails, CacheManifest, Category, Collection, Item, JobDetails, MissingItem} from 'brooke-domain';

@Injectable()
export class BrookeServerService {
  constructor(private http: HttpClient) { }

  getCollections(): Observable<Collection[]> {
    return this.http.get<Collection[]>(
      '/rest/collection'
    );
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

	copyToTablet(collectionName: string, itemName: string): Observable<JobDetails> {
		return this.http.get<JobDetails>(
			`/rest/copy-to-tablet/${collectionName}/${itemName}`
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

	getCachedManifest(): Observable<CacheManifest> {
		return this.http.get<CacheManifest>(`/rest/cache`);
	}

	getMissingItems(): Observable<MissingItem[]> {
		return this.http.get<MissingItem[]>(
      `/rest/administration/missing-item`
    );
	}
}