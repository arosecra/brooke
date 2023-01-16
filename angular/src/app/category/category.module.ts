import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoryComponent } from './category.component';
import { MaterialModule } from '../material/material.module';
import { ItemModule } from '../item/item.module';
import { CategoryMenuModule } from '../category-menu/category-menu.module';
import { TopBarModule } from '../top-bar/top-bar.module';
import { CollectionMenuModule } from '../collection-menu/collection-menu.module';



@NgModule({
  declarations: [
    CategoryComponent
  ],
  imports: [
    CommonModule,
    ItemModule,
    MaterialModule,
    CategoryMenuModule,
    TopBarModule,
    CollectionMenuModule,
  ]
})
export class CategoryModule { }
