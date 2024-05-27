import {HttpEvent, HttpEventType, HttpRequest, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, Observer} from 'rxjs';


import {HttpBackend, ɵREQUESTS_CONTRIBUTE_TO_STABILITY} from '@angular/common/http';
import {Provider} from '@angular/core';
import { HttpTestingController, TestRequest } from '@angular/common/http/testing';
import { Category, Collection } from 'src/app/brooke/brooke.model';

// Based off of Angulars provideHttpClientTesting()

export function provideBackend(): Provider[] {
  return [
    Backend,
    {provide: HttpBackend, useExisting: Backend},
    {provide: ɵREQUESTS_CONTRIBUTE_TO_STABILITY, useValue: false},
  ];
}


@Injectable()
export class Backend implements HttpBackend {


  /**
   * Handle an incoming request by queueing it in the list of open requests.
   */
  handle(req: HttpRequest<any>): Observable<HttpEvent<any>> {
    return new Observable((observer: Observer<any>) => {

			let body: any = ''
			if(req.url.startsWith('/rest/collection')) {
				let collection: Collection = {
					name: 'test-collection',
					remoteDirectory: 'T',
					localDirectory: 'L',
					itemExtension: '.cbz',
					excludeExtensions: [],
					openType: 'book',
					autoGenerateAlphaCategories: false,
					pipelineSteps: [],
					categories: [{
						name: 'test_category',
						items: []
					} as Category]
				}
			} else if (req.url.startsWith('')) {

			}

      observer.next(new HttpResponse({
                  body,
                  headers: undefined,
                  status: 200,
                  statusText: 'OK',
                  url: req.url,
                }));
			observer.complete();
    });
  }

}