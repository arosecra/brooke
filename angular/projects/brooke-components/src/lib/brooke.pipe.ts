import { Pipe, PipeTransform } from '@angular/core';
import { Category, Collection, Item } from 'brooke-domain';

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
