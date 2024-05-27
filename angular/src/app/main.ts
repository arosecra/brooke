import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './brooke/app.config';
import { AppComponent } from './brooke/app.component';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
