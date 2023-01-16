import { Component, OnInit } from '@angular/core';
import { BrookeService } from '../brooke.service';
import { map, Observable } from 'rxjs';

interface Link {
  displayName: string;
  routerLink: string;
  queryParams?: any;
}

interface CollectionMenuVM {
  links: Link[];
}

const home: Link = {
  displayName: 'Home',
  routerLink: '/home'
};

@Component({
  selector: 'app-collection-menu',
  templateUrl: './collection-menu.component.html'
})
export class CollectionMenuComponent implements OnInit {

  vm$: Observable<CollectionMenuVM>;

  links = [
    {
      route: 'home',
      displayName: 'Home',
      routerLink: '/home',
      isActive: false
    },
    {
      displayName: "Anime",
      routerLink: '/collection',
      queryParams: { collection: 'Anime' },
      isActive: false
    }, {
      displayName: "Movies",
      routerLink: '/collection',
      queryParams: { collection: 'Movies' },
      isActive: false
    }
  ]

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
        })

        return {
          collections: collections,
          links: links
        }

      })

    )
  }
}
