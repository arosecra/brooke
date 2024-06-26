import { Pipe, PipeTransform } from '@angular/core';
import { Item, Collection, Category } from './brooke.model';

@Pipe({
  name: 'itemName',
  standalone: true
})
export class ItemNamePipe implements PipeTransform {

  transform(value: Item, 
		collection: Collection | undefined, 
		category: Category | undefined, 
		replaceCategoryName: boolean,
		...args: unknown[]
	): unknown {
		let result = value.name;
		let categoryName = category?.name ?? ''
		if(replaceCategoryName && categoryName.length > 1) {
			result = result.replace(categoryName, '');
		} 
		
    return result.replaceAll('_', ' ');
  }

}
