import { ChangeDetectionStrategy, Component, computed } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { RouterModule } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { TabNavItem } from '@dv/shared/util/navigation';

// gesuch, darlehen, fehlgeschlagene zahlungen
const tabs: TabNavItem[] = [
  {
    name: 'gesuche',
    route: ['gesuche'],
    active: false,
    queryParams: { tab: 'gesuche' },
  },
  {
    name: 'pendent',
    route: ['gesuche'],
    active: false,
    queryParams: { tab: 'pendent' },
  },
  {
    name: 'verfuegungen-druck',
    route: ['gesuche'],
    queryParams: { tab: 'verfuegungen-druck' },
    active: false,
  },
  {
    name: 'datenschutz-briefe-druck',
    route: ['gesuche'],
    queryParams: { tab: 'datenschutz-briefe-druck' },
    active: false,
  },
  {
    name: 'darlehen',
    route: ['darlehen'],
    active: false,
  },
  {
    name: 'fehlgeschlagene-zahlungen',
    route: ['fehlgeschlagene-zahlungen'],
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
}
