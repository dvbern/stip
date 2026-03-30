import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { TabNavItem } from '@dv/shared/util/navigation';

// todo: add permissions
const tabs: TabNavItem[] = [
  {
    name: 'gesuche',
    route: ['gesuche'],
    queryParams: { filterTab: 'GESUCHE' },
    queryParamsHandling: 'merge',
    active: false,
  },
  {
    name: 'pendent',
    route: ['gesuche'],
    queryParams: { filterTab: 'PENDENTE_GESUCHE' },
    queryParamsHandling: 'merge',
    active: false,
  },
  {
    name: 'verfuegungen-druck',
    route: ['gesuche'],
    queryParams: { filterTab: 'DRUCKBAR_VERFUEGUNGEN' },
    queryParamsHandling: 'merge',
    active: false,
  },
  {
    name: 'datenschutz-briefe-druck',
    route: ['gesuche'],
    queryParams: { filterTab: 'DRUCKBAR_DATENSCHUTZBRIEFE' },
    queryParamsHandling: 'merge',
    active: false,
  },
  {
    name: 'darlehen',
    route: ['darlehen'],
    queryParamsHandling: 'merge',
    active: false,
  },
  {
    name: 'fehlgeschlagene-zahlungen',
    route: ['fehlgeschlagene-zahlungen'],
    queryParamsHandling: 'merge',
    active: false,
  },
];

@Component({
  selector: 'dv-sachbearbeitung-app-feature-dashboard',
  imports: [TranslocoDirective, MatTabsModule, RouterModule],
  templateUrl: './sachbearbeitung-app-feature-dashboard.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureDashboardComponent {
  router = inject(Router);
  route = inject(ActivatedRoute);
  filterTab = input<string | undefined>(undefined);

  // todo: filter by role
  tabsSig = computed<TabNavItem[]>(() => {
    // const gesuchId = this.gesuchIdSig();
    // const trancheId = this.trancheIdSig();
    // const { gesuchInfo } = this.headerViewSig();
    // const activePath = this.routeUrlSig();

    // todo-review: @scph oder lieber mit trancheSetting und ngrx store?
    // const trancheTyp = isIntitial
    //   ? 'initial'
    //   : isAenderung
    //     ? 'aenderung'
    //     : 'tranche';

    // if (!this.isGesuchRouteSig()) {
    //   return [];
    // }

    // const gesuchTab = {
    //   active: !activePath?.includes('/verfuegung'),
    //   route: ['/gesuch', gesuchId, trancheTyp, trancheId],
    //   queryParams: { berechnungId },
    //   name: 'formular',
    // };

    // const verfuegungTab = {
    //   active: activePath?.includes('/verfuegung'),
    //   route: ['/gesuch/verfuegung', gesuchId, trancheTyp, trancheId],
    //   queryParams: { berechnungId },
    //   name: 'verfuegung',
    // };

    // if (gesuchInfo?.state.canGetBerechnung) {
    //   return [gesuchTab, verfuegungTab];
    // }

    // return [gesuchTab];

    return tabs;
  });

  constructor() {
    // add default filterTab to the routes if not already set
    // this does not work and does not seem to be the right place
    // do in parent, or do in onInit()
    // effect(() => {
    //   const filterTab = this.filterTab();
    //   if (!filterTab) {
    //     // navigate to default tab
    //     // this will not work as expected, because the orirignal navigation
    //     // will be fulfilled first. This will lead to multiple requests.
    //     this.router.navigate(['gesuche'], {
    //       relativeTo: this.route,
    //       queryParams: { filterTab: 'GESUCHE', scope: 'ALLE', work: 'GESUCHE' },
    //       queryParamsHandling: 'merge',
    //     });
    //   }
    // });
  }
}
