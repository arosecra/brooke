import { Pipe, PipeTransform } from '@angular/core';
import { Item, NewCollection, NewCategory } from '../app-model';

@Pipe({
  name: 'itemName',
  standalone: true,
})
export class ItemNamePipe implements PipeTransform {
  transform(
    value: Item | string | undefined,
    collection?: NewCollection | undefined,
    category?: NewCategory | undefined,
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
