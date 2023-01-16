import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { MaterialModule } from '../material/material.module';
import { RouterModule } from '@angular/router';
import { TopBarModule } from '../top-bar/top-bar.module';
import { CollectionMenuModule } from '../collection-menu/collection-menu.module';



@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CollectionMenuModule,
    CommonModule, 
    MaterialModule,
    RouterModule,
    TopBarModule,
  ]
})
export class HomeModule { }
