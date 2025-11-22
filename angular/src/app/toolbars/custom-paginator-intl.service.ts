import { inject, Injectable } from "@angular/core";
import { MatPaginatorIntl } from "@angular/material/paginator";
import { AppComponent } from "../app.component";

@Injectable()
export class CustomPaginatorIntl extends MatPaginatorIntl {
	app = inject(AppComponent);

  override getRangeLabel = (page: number, pageSize: number, length: number): string => {
    if (length === 0) {
      return `Page 1 of 1`;
    }
		if(this.app.widgets().book.pagesInDisplay() === 1) {
    	return `Page ${page * pageSize + 1} of ${length}`;
		} else {
    	return `Pages ${page * pageSize + 1}, ${page * pageSize + 2} of ${length}`;
		}
  }
}