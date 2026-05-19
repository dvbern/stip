import { Route } from '@angular/router';

import { BfsStatistikStore } from '@dv/sachbearbeitung-app/data-access/bfs-statistik';
import { OPTION_BFS_STATISTIK } from '@dv/sachbearbeitung-app/model/administration';

import { SachbearbeitungAppFeatureAdministrationBfsStatistikComponent } from './sachbearbeitung-app-feature-administration-bfs-statistik/sachbearbeitung-app-feature-administration-bfs-statistik.component';

export const sachbearbeitungAppFeatureAdministrationBfsStatistikRoutes: Route[] =
  [
    {
      path: '',
      pathMatch: 'prefix',
      providers: [BfsStatistikStore],
      data: { option: OPTION_BFS_STATISTIK },
      children: [
        {
          path: '',
          component:
            SachbearbeitungAppFeatureAdministrationBfsStatistikComponent,
        },
      ],
    },
  ];
