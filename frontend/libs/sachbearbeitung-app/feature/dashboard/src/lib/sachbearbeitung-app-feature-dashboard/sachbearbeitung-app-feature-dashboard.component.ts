import { CommonModule } from '@angular/common';
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
import { SortAndPageInputs } from '@dv/shared/model/table';
import { DEFAULT_PAGE_SIZE } from '@dv/shared/model/ui-constants';
import {
  DashboardFilterTabItem,
  DashboardTableEntryFields,
  getDefaultQueryForRole,
} from '@dv/shared/util/dashboard';

const resetTableFilterObj: Record<
  DashboardTableEntryFields | keyof SortAndPageInputs<unknown>,
  undefined
> = {
  fallNummer: undefined,
  typ: undefined,
  piaNachname: undefined,
  piaVorname: undefined,
  piaGeburtsdatum: undefined,
  bearbeiter: undefined,
  letzteAktivitaet: undefined,
  status: undefined,
  sortColumn: undefined,
  sortOrder: undefined,
  page: undefined,
  pageSize: undefined,
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
  imports: [CommonModule, TranslocoDirective, MatTabsModule, RouterModule],
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

  tabsSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();
    const filterTab = this.filterTab();
    const hasfehlgeschlageneZahlungen =
      this.fehlgeschlageneZahlungenStore.hasFehlgeschalgeneZahlungenSig();

    const tabs = baseFilterTabs
      .concat(
        hasfehlgeschlageneZahlungen
          ? [
              {
                key: 'fehlgeschlagene-zahlungen',
                route: ['fehlgeschlagene-zahlungen'],
                queryParamsHandling: 'merge',
                class: 'tw:border-3! tw:border-red-500! tw-ml-auto!',
                active: false,
                roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
              },
            ]
          : [],
      )
      .filter((tab) => {
        if (!tab.roles || tab.roles.length === 0) {
          return true;
        }

        return tab.roles.some((role) => rolesMap[role]);
      });

    return tabs.map((tab) => ({
      ...tab,
      active: tab.queryParams?.['filterTab'] === filterTab,
      // reset table filters when switching filter tabs
      queryParams: { ...resetTableFilterObj, ...tab.queryParams },
    }));
  });

  constructor() {
    this.fehlgeschlageneZahlungenStore.getFehlgeschlageneZahlungen$({
      page: 1,
      pageSize: DEFAULT_PAGE_SIZE,
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
