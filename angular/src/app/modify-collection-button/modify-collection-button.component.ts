import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, of, switchMap } from 'rxjs';
import { BrookeService } from '../brooke.service';

interface ModifyCollectionButtonVM {
	collection: string;
	pageState: ModifyCollectionButtonPageState;
}

interface ModifyCollectionButtonPageState {
	visible: boolean;
}

@Component({
  selector: 'app-modify-collection-button',
  templateUrl: './modify-collection-button.component.html'
})
export class ModifyCollectionButtonComponent {
  vm$: Observable<ModifyCollectionButtonVM>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private brookeService: BrookeService,
    private router: Router,
  ) { }

  ngOnInit() {

    this.vm$ = this.activatedRoute.queryParams.pipe(
      map((queryParams) => {
        return {
					collection: queryParams['collection'],
					pageState: {
						visible: queryParams['collection'] && !queryParams['category']
					}
				}

      })
    )
  }

	onEditCollectionClick(vm: ModifyCollectionButtonVM) {
		this.router.navigate(['/modify-collection'], {
			queryParams: {
				collection: vm.collection
			}
		})
	}
}
