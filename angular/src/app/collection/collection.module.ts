import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CollectionComponent } from './collection.component';
import { RouterModule } from '@angular/router';
import { TopBarModule } from '../top-bar/top-bar.module';
import { CollectionMenuModule } from '../collection-menu/collection-menu.module';



@NgModule({
  declarations: [
    CollectionComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    TopBarModule,
    CollectionMenuModule,
  ]
})
export class CollectionModule { }
