import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CollectionMenuModule } from '../collection-menu/collection-menu.module';
import { TopBarModule } from '../top-bar/top-bar.module';
import { ModifyCollectionComponent } from './modify-collection.component';



@NgModule({
  declarations: [
    ModifyCollectionComponent
  ],
  imports: [
    CommonModule, 
    CollectionMenuModule,
    RouterModule,
    TopBarModule
  ],
  exports: [
    ModifyCollectionComponent
  ]
})
export class ModifyCollectionModule { }
