import { bootstrapApplication } from '@angular/platform-browser';

import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';

// TODO: cleanup, used to build affected +1
bootstrapApplication(AppComponent, appConfig).catch((err) =>
  console.error(err),
);
