import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
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
    RouterModule,
    TopBarModule
  ]
})
export class HomeModule { }
