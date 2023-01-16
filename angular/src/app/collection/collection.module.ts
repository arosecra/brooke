import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CollectionComponent } from './collection.component';
import { MaterialModule } from '../material/material.module';
import { RouterModule } from '@angular/router';
import { CategoryMenuModule } from '../category-menu/category-menu.module';
import { TopBarModule } from '../top-bar/top-bar.module';
import { CollectionMenuModule } from '../collection-menu/collection-menu.module';



@NgModule({
  declarations: [
    CollectionComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    RouterModule,
    CategoryMenuModule,
    TopBarModule,
    CollectionMenuModule,
  ]
})
export class CollectionModule { }
