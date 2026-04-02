import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  inject,
  input,
} from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { FehlgeschlageneZahlungenStore } from '@dv/sachbearbeitung-app/data-access/fehlgeschlagene-zahlungen';
import { PermissionStore } from '@dv/shared/global/permission';
import { DashboardFilterTabItem, TabNavItem } from '@dv/shared/util/navigation';

const baseFilterTabs: DashboardFilterTabItem[] = [
  {
    name: 'gesuche',
    route: ['gesuche'],
    queryParams: { filterTab: 'GESUCHE' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle', 'V0_Jurist'],
  },
  {
    name: 'pendent',
    route: ['gesuche'],
    queryParams: { filterTab: 'PENDENTE_GESUCHE' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    name: 'verfuegungen-druck',
    route: ['gesuche'],
    queryParams: { filterTab: 'DRUCKBAR_VERFUEGUNGEN' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    name: 'datenschutz-briefe-druck',
    route: ['gesuche'],
    queryParams: { filterTab: 'DRUCKBAR_DATENSCHUTZBRIEFE' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    name: 'juristische-abklaerung',
    route: ['gesuche'],
    queryParams: { filterTab: 'JURISTISCHE_ABKLAERUNG' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Jurist'],
  },
  {
    name: 'abklaerung-durch-rechtsabteilung',
    route: ['gesuche'],
    queryParams: { filterTab: 'ABKLAERUNG_DURCH_RECHSTABTEILUNG' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Jurist'],
  },
  {
    name: 'darlehen',
    route: ['darlehen'],
    queryParams: { filterTab: 'DARLEHEN' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    name: 'fehlgeschlagene-zahlungen',
    route: ['fehlgeschlagene-zahlungen'],
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
];

@Component({
  selector: 'dv-sachbearbeitung-app-feature-dashboard',
  imports: [TranslocoDirective, MatTabsModule, RouterModule],
  templateUrl: './sachbearbeitung-app-feature-dashboard.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureDashboardComponent {
  @HostBinding('class') klass = 'tw:px-6 tw:dv-pass-height';

  private permissionStore = inject(PermissionStore);
  private fehlgeschlageneZahlungenStore = inject(FehlgeschlageneZahlungenStore);

  router = inject(Router);
  route = inject(ActivatedRoute);
  filterTab = input<string | undefined>(undefined);

  tabsSig = computed<TabNavItem[]>(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const filterTab = this.filterTab();

    const tabs = baseFilterTabs.filter((tab) => {
      if (!tab.roles || tab.roles.length === 0) {
        return true;
      }

      return tab.roles.some((role) => rolesMap[role]);
    });

    if (this.fehlgeschlageneZahlungenStore.hasFehlgeschalgeneZahlungenSig()) {
      tabs.push({
        name: 'fehlgeschlagene-zahlungen',
        route: ['fehlgeschlagene-zahlungen'],
        queryParamsHandling: 'merge',
        active: false,
        roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
      });
    }

    return tabs.map((tab) => ({
      ...tab,
      active: tab.queryParams?.['filterTab'] === filterTab,
    }));
  });

  constructor() {
    this.fehlgeschlageneZahlungenStore.getFehlgeschlageneZahlungen$({
      page: 1,
      pageSize: 10,
    });
  }
}
