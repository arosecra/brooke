import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CachingComponent } from './caching.component';
import {NgxFilesizeModule} from 'ngx-filesize';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [
    CachingComponent
  ],
  imports: [
    CommonModule,
		RouterModule,
		NgxFilesizeModule,
  ],
  exports: [
    CachingComponent
  ]
})
export class CachingModule { }
