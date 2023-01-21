import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CollectionMenuComponent } from './collection-menu.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    CollectionMenuComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    CollectionMenuComponent
  ]
})
export class CollectionMenuModule { }
