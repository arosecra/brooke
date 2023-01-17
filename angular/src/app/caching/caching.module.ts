import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CachingComponent } from './caching.component';
import {NgxFilesizeModule} from 'ngx-filesize';


@NgModule({
  declarations: [
    CachingComponent
  ],
  imports: [
    CommonModule,
		NgxFilesizeModule,
  ],
  exports: [
    CachingComponent
  ]
})
export class CachingModule { }
