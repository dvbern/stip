import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  HostBinding,
  computed,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatTabsModule } from '@angular/material/tabs';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { FehlgeschlageneZahlungenStore } from '@dv/sachbearbeitung-app/data-access/fehlgeschlagene-zahlungen';
import { PermissionStore } from '@dv/shared/global/permission';
import {
  DashboardFilterTabItem,
  DashboardTableEntryFields,
  getDefaultQueryForRole,
} from '@dv/shared/util/dashboard';
import { TabNavItem } from '@dv/shared/util/navigation';

const resetTableFilterObj: Record<DashboardTableEntryFields, undefined> = {
  fallNummer: undefined,
  typ: undefined,
  piaNachname: undefined,
  piaVorname: undefined,
  piaGeburtsdatum: undefined,
  bearbeiter: undefined,
  letzteAktivitaet: undefined,
  status: undefined,
};

const baseFilterTabs: DashboardFilterTabItem[] = [
  {
    key: 'gesuche',
    route: ['gesuche'],
    queryParams: { filterTab: 'GESUCHE' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle', 'V0_Jurist'],
  },
  {
    key: 'pendent',
    route: ['gesuche'],
    queryParams: { filterTab: 'PENDENTE_GESUCHE' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    key: 'verfuegungen-druck',
    route: ['gesuche'],
    queryParams: { filterTab: 'DRUCKBAR_VERFUEGUNGEN' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    key: 'datenschutz-briefe-druck',
    route: ['gesuche'],
    queryParams: { filterTab: 'DRUCKBAR_DATENSCHUTZBRIEFE' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    key: 'juristische-abklaerung',
    route: ['gesuche'],
    queryParams: { filterTab: 'JURISTISCHE_ABKLAERUNG' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Jurist'],
  },
  {
    key: 'abklaerung-durch-rechtsabteilung',
    route: ['gesuche'],
    queryParams: { filterTab: 'ABKLAERUNG_DURCH_RECHSTABTEILUNG' },
    queryParamsHandling: 'merge',
    active: false,
    roles: ['V0_Jurist'],
  },
  {
    key: 'darlehen',
    route: ['gesuche'],
    queryParams: { filterTab: 'DARLEHEN' },
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
  destroyRef = inject(DestroyRef);
  filterTab = input<string | undefined>(undefined);

  tabsSig = computed<TabNavItem[]>(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const filterTab = this.filterTab();
    const hasfehlgeschlageneZahlungen =
      this.fehlgeschlageneZahlungenStore.hasFehlgeschalgeneZahlungenSig();

    const tabs = baseFilterTabs.filter((tab) => {
      if (!tab.roles || tab.roles.length === 0) {
        return true;
      }

      return tab.roles.some((role) => rolesMap[role]);
    });

    if (hasfehlgeschlageneZahlungen) {
      tabs.push({
        key: 'fehlgeschlagene-zahlungen',
        route: ['fehlgeschlagene-zahlungen'],
        queryParamsHandling: 'merge',
        active: false,
        roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
      });
    }

    return tabs.map((tab) => ({
      ...tab,
      active: tab.queryParams?.['filterTab'] === filterTab,
      // reset table filters when switching tabs
      // queryParams: { ...resetTableFilterObj, ...tab.queryParams },
    }));
  });

  constructor() {
    this.fehlgeschlageneZahlungenStore.getFehlgeschlageneZahlungen$({
      page: 1,
      // todo: @scph is this assumption "good" it used to be 10!
      pageSize: 100,
    });

    const defaultFilter = getDefaultQueryForRole(
      this.permissionStore.rolesMapSig(),
    );

    // ensure that a default query is present
    this.route.queryParams
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((params) => {
        if (!params['filterTab']) {
          this.router.navigate(['antraege'], {
            relativeTo: this.route,
            queryParams: {
              filterTab: defaultFilter.filterTab,
              scope: defaultFilter.scope,
              workable: defaultFilter.workable,
            },
            queryParamsHandling: 'merge',
            replaceUrl: true,
          });
        }
      });
  }
}
