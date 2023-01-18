import { Component, OnInit } from '@angular/core';
import { BrookeService } from '../brooke.service';
import { map, Observable } from 'rxjs';

interface Link {
  displayName: string;
  routerLink: string;
  queryParams?: any;
	subLinks?: Link[];
}

interface CollectionMenuVM {
  links: Link[];
}

const home: Link = {
  displayName: 'Home',
  routerLink: '/home'
};

const admin: Link = {
	displayName: 'Administration',
	routerLink: '',
	subLinks: [
		{
			displayName: 'Missing Items',
			routerLink: '/missing-items'
		},
		{
			displayName: 'Synchronize',
			routerLink: '/synchronize'
		},
	]
}

@Component({
  selector: 'app-collection-menu',
  templateUrl: './collection-menu.component.html'
})
export class CollectionMenuComponent implements OnInit {

  vm$: Observable<CollectionMenuVM>;

  constructor(
    private brookeService: BrookeService
  ) {
  }


  ngOnInit(): void {
    this.vm$ = this.brookeService.getCollections().pipe(
      map((collections) => {
        let links: Link[] = [];
        links.push(home);

        collections.forEach((collection) => {
          links.push(
            {
              displayName: collection.name.replaceAll('_', ' '),
              routerLink: '/collection',
              queryParams: { collection: collection.name }
            }
          )
        });

				links.push(admin);

        return {
          collections: collections,
          links: links
        }

      })

    )
  }
}
