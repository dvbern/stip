import { Route } from '@angular/router';

import { AdministrationAusbildungsstaetteStore } from '@dv/sachbearbeitung-app/data-access/administration-ausbildungsstaette';
import { OPTION_AUSBILDUNGSSTAETTE } from '@dv/sachbearbeitung-app/model/administration';
import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';

import { SachbearbeitungAppFeatureAdministrationAusbildungsstaetteComponent } from './sachbearbeitung-app-feature-administration-ausbildungsstaette/sachbearbeitung-app-feature-administration-ausbildungsstaette.component';

export const sachbearbeitungAppFeatureAdministrationAusbildungsstaetteRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      data: { option: OPTION_AUSBILDUNGSSTAETTE },
      providers: [
        AusbildungsstaetteStore,
        AdministrationAusbildungsstaetteStore,
      ],
      component:
        SachbearbeitungAppFeatureAdministrationAusbildungsstaetteComponent,
      children: [
        {
          path: '',
          pathMatch: 'full',
          redirectTo: 'ausbildungsgang',
        },
        {
          path: 'ausbildungsgang',
          loadComponent: () =>
            import(
              './components/ausbildungsgang/ausbildungsgang.component'
            ).then((m) => m.AusbildungsgangComponent),
        },
        {
          path: 'ausbildungsstaette',
          loadComponent: () =>
            import(
              './components/ausbildungsstaette/ausbildungsstaette.component'
            ).then((m) => m.AusbildungsstaetteComponent),
        },
        {
          path: 'abschluss',
          loadComponent: () =>
            import('./components/abschluss/abschluss.component').then(
              (m) => m.AbschlussComponent,
            ),
        },
      ],
    },
  ];
