import { inject } from '@angular/core';
import { Route } from '@angular/router';

import { SachbearbeiterStore } from '@dv/sachbearbeitung-app/data-access/sachbearbeiter';
import { OPTION_BUCHSTABEN_ZUTEILUNG } from '@dv/sachbearbeitung-app/model/administration';
import { routeWithUnsavedChangesGuard } from '@dv/shared/pattern/unsaved-guard';

import { SachbearbeitungAppFeatureAdministrationBuchstabenZuteilungComponent } from './sachbearbeitung-app-feature-administration-buchstaben-zuteilung/sachbearbeitung-app-feature-administration-buchstaben-zuteilung.component';

export const sachbearbeitungAppFeatureAdministrationBuchstabenZuteilungRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      data: { option: OPTION_BUCHSTABEN_ZUTEILUNG },
      resolve: {
        init: () => {
          inject(SachbearbeiterStore).loadSachbearbeiterZuweisung();
        },
      },
      providers: [SachbearbeiterStore],
      children: [
        routeWithUnsavedChangesGuard({
          path: '',
          component:
            SachbearbeitungAppFeatureAdministrationBuchstabenZuteilungComponent,
        }),
      ],
    },
  ];
