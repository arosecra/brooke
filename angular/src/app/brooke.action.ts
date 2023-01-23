import { createAction, props } from '@ngrx/store';
import { Category, Collection } from './brooke.model';

export const getCollections = 
	createAction(
		'[Brooke] Get Collections'
	);

export const openCollection = 
	createAction(
		'[Brooke] Open Collection',
		props<{ collection: Collection }>()
	);

export const openCategory = 
	createAction(
		'[Brooke] Open Category',
		props<{category: Category}>()
	);