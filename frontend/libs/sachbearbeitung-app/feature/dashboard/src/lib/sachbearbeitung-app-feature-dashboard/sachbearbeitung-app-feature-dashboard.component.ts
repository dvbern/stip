import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatTabsModule } from '@angular/material/tabs';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';
import { map } from 'rxjs';

import { PermissionStore } from '@dv/shared/global/permission';
import { DashboardFilterTabItem, TabNavItem } from '@dv/shared/util/navigation';

const tabs: DashboardFilterTabItem[] = [
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
    route: ['juristische-abklaerung'],
    queryParams: { filterTab: 'JURISTISCHE_ABKLAERUNG' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Jurist'],
  },
  {
    name: 'abklaerung-durch-rechtsabteilung',
    route: ['abklaerung-durch-rechtsabteilung'],
    queryParams: { filterTab: 'ABKLAERUNG_DURCH_RECHSTABTEILUNG' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Jurist'],
  },
  {
    name: 'darlehen',
    route: ['darlehen'],
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

  router = inject(Router);
  route = inject(ActivatedRoute);
  filterTab = input<string | undefined>(undefined);

  filterTabQueryParam = toSignal(
    this.route.queryParamMap.pipe(
      map((params) => params.get('filterTab') ?? undefined),
    ),
  );

  tabsSig = computed<TabNavItem[]>(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const filterTab = this.filterTabQueryParam();
    return tabs
      .filter((tab) => {
        if (!tab.roles || tab.roles.length === 0) {
          return true;
        }

        return tab.roles.some((role) => rolesMap[role]);
      })
      .map((tab) => ({
        ...tab,
        active: tab.queryParams?.['filterTab'] === filterTab,
      }));
  });
}
