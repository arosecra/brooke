import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MissingItemsComponent } from './missing-items.component';



@NgModule({
  declarations: [
    MissingItemsComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    MissingItemsComponent
  ]
})
export class MissingItemsModule { }
