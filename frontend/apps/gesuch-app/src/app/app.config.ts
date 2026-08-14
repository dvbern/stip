import { ApplicationConfig } from '@angular/core';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

import {
  sharedDataAccessGesuchEffects,
  sharedDataAccessGesuchsFeature,
} from '@dv/shared/data-access/gesuch';
import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import { provideSharedPatternCore } from '@dv/shared/pattern/core';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideSharedPatternCore(routes, {
      type: 'gesuch-app',
      view: 'gesuchsteller',
      keyPrefix: 'gesuch-app',
    }),
    SozialdienstStore,
    provideState(sharedDataAccessGesuchsFeature),
    provideEffects(sharedDataAccessGesuchEffects),
    provideAnimations(),
  ],
};
