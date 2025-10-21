import { Pipe, PipeTransform } from '@angular/core';
import { Item } from '../model/item';
import { Category } from '../model/category';
import { Collection } from '../model/collection';

@Pipe({
  name: 'itemName',
  standalone: true,
})
export class ItemNamePipe implements PipeTransform {
  transform(
    value: Item | string | undefined,
    collection?: Collection | undefined,
    category?: Category | undefined,
    replaceCategoryName?: boolean,
    ...args: unknown[]
  ): unknown {
    let result = value ? (typeof value === 'string' ? value : value.name) : '';
    let categoryName = category?.name ?? '';
    if (replaceCategoryName && categoryName.length > 1) {
      result = result.replace(categoryName, '');
    }

    return result.replaceAll('_', ' ');
  }
}
