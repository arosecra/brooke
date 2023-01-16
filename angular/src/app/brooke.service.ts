import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import {BookDetails, Category, Collection, Item, JobDetails} from './brooke.model';

@Injectable()
export class BrookeService {
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
}