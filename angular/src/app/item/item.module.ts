import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ItemComponent } from './item.component';
import { MaterialModule } from '../material/material.module';



@NgModule({
  declarations: [
    ItemComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
  ],
  exports: [
    ItemComponent
  ]
})
export class ItemModule { }
