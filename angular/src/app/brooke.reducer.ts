import { createReducer, on } from '@ngrx/store';
import { getCollections, openCategory, openCollection } from './brooke.action';
import { Category, Collection, Item, JobDetails } from './brooke.model';

export declare interface BrookeState {
	collection?: Collection,
	category?: Category,
	series?: Item,
	item?: Item,
	collections?: Collection[],
	currentJob?: JobDetails
}

export const initialState: BrookeState = {
	collection: undefined,
	category: undefined,
	series: undefined,
	item: undefined,
	collections: [],
	currentJob: undefined
}

export const brookeReducer = createReducer(
	initialState,

	on(getCollections, (state, action) => ({
		...state
	})),

	on(openCollection, (state, action) => ({
		...state,
		selectedCollection: action.collection,
	})),

	on(openCategory, (state, action) => ({
		...state,
		selectedCategory: action.category
	}))
)

export const selectCollections = (state: BrookeState) => state.collections;
export const selectCollection = (state: BrookeState) => state.collection;

export const selectCategory = (state: BrookeState) => state.category;