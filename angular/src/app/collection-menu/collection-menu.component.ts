import { Component, OnInit } from '@angular/core';
import { BrookeService } from '../brooke.service';
import { map, Observable, switchMap } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

interface Link {
	displayName: string;
	isActive: boolean;
	routerLink?: string;
	queryParams?: any;
	subLinks?: Link[];
}

interface CollectionMenuVM {
	links: Link[];
}

const home: Link = {
	displayName: 'Home',
	routerLink: '/home',
	isActive: false
};

const admin: Link = {
	displayName: 'Administration',
	isActive: false,
	subLinks: [
		{
			displayName: 'Missing Items',
			routerLink: '/missing-items',
			isActive: false
		},
		{
			displayName: 'Synchronize',
			routerLink: '/synchronize',
			isActive: false
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
		private activatedRoute: ActivatedRoute,
		private brookeService: BrookeService
	) {
	}

	ngOnInit(): void {

		this.vm$ = this.activatedRoute.queryParams.pipe(
			switchMap((queryParams) => {
				return this.brookeService.getCollections().pipe(
					map((collections) => {
						let links: Link[] = [];
						links.push(home);

						collections.forEach((collection) => {
							let link: Link = {
								displayName: collection.name.replaceAll('_', ' '),
								isActive: queryParams['collection'] === collection.name,
								routerLink: '/collection',
								queryParams: { collection: collection.name }
							}

							if (link.isActive) {
								link.routerLink = '/home'
								link.queryParams = {}
							}

							link.subLinks = []
							collection.categories.forEach((category) => {
								let sublink: Link = {
									displayName: category.name.replaceAll('_', ' '),
									isActive: queryParams['category'] === category.name,
									routerLink: '/category',
									queryParams: {
										collection: collection.name,
										category: category.name
									}
								}

								if (sublink.isActive) {
									sublink.routerLink = '/collection';
									sublink.queryParams = {
										collection: collection.name
									};
								}

								link.subLinks?.push(sublink);
							})

							links.push(link)
						});

						links.push(admin);

						return {
							collections: collections,
							links: links
						} as CollectionMenuVM;

					})
				)
			}
			)
		)
	}
}
