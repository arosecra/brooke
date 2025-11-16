import { Injectable } from "@angular/core";
import { MatPaginatorIntl } from "@angular/material/paginator";

@Injectable()
export class CustomPaginatorIntl extends MatPaginatorIntl {

  override getRangeLabel = (page: number, pageSize: number, length: number): string => {
    if (length === 0) {
      return `Page 1 of 1`;
    }
    return `Page ${page * pageSize + 1} of ${length}`;
  }
}