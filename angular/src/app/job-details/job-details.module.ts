import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobDetailsComponent } from './job-details.component';
import { NgxFilesizeModule } from 'ngx-filesize';



@NgModule({
  declarations: [
    JobDetailsComponent
  ],
  imports: [
    CommonModule,
		NgxFilesizeModule,
  ],
  exports: [
    JobDetailsComponent
  ]
})
export class JobDetailsModule { }
