import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SeriesComponent } from './series.component';
import { MaterialModule } from '../material/material.module';
import { RouterModule } from '@angular/router';
import { TopBarModule } from '../top-bar/top-bar.module';



@NgModule({
  declarations: [
    SeriesComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    RouterModule,
    TopBarModule
  ]
})
export class SeriesModule { }
