import { ApplicationConfig } from '@angular/core';
import { provideNativeDateAdapter } from '@angular/material/core';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideEffects } from '@ngrx/effects';
import { provideState } from '@ngrx/store';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { ChangeGesuchsperiodeStore } from '@dv/shared/data-access/change-gesuchsperiode';
import {
  sharedDataAccessGesuchEffects,
  sharedDataAccessGesuchsFeature,
} from '@dv/shared/data-access/gesuch';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { provideSharedPatternCore } from '@dv/shared/pattern/core';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideSharedPatternCore(routes, {
      authClientId: 'stip-sachbearbeitung-app',
      appType: 'sachbearbeitung-app',
    }),
    provideNativeDateAdapter(),
    provideState(sharedDataAccessGesuchsFeature),
    provideEffects(sharedDataAccessGesuchEffects),
    provideAnimations(),
    GesuchStore,
    GesuchInfoStore,
    ChangeGesuchsperiodeStore,
  ],
};
