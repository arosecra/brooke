import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SeriesComponent } from './series.component';
import { RouterModule } from '@angular/router';
import { TopBarModule } from '../top-bar/top-bar.module';



@NgModule({
  declarations: [
    SeriesComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    TopBarModule
  ]
})
export class SeriesModule { }
