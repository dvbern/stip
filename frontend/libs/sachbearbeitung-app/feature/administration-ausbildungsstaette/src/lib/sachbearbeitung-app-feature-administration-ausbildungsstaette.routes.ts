import { Route } from '@angular/router';

import { OPTION_AUSBILDUNGSSTAETTE } from '@dv/sachbearbeitung-app/model/administration';
import { checkUnsavedChanges } from '@dv/shared/pattern/unsaved-guard';

import { SachbearbeitungAppFeatureAdministrationAusbildungsstaetteComponent } from './sachbearbeitung-app-feature-administration-ausbildungsstaette/sachbearbeitung-app-feature-administration-ausbildungsstaette.component';

export const sachbearbeitungAppFeatureAdministrationAusbildungsstaetteRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      data: { option: OPTION_AUSBILDUNGSSTAETTE },
      children: [
        {
          path: '',
          component:
            SachbearbeitungAppFeatureAdministrationAusbildungsstaetteComponent,
          canDeactivate: [checkUnsavedChanges],
        },
      ],
    },
  ];
