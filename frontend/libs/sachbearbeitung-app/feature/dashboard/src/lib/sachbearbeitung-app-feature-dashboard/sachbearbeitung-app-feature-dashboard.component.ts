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
import { MatTabsModule } from '@angular/material/tabs';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import { FehlgeschlageneZahlungenStore } from '@dv/sachbearbeitung-app/data-access/fehlgeschlagene-zahlungen';
import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import { PermissionStore } from '@dv/shared/global/permission';
import { AvailableBenutzerRole } from '@dv/shared/model/benutzer';
import { SortAndPageInputs } from '@dv/shared/model/table';
import {
  DashboardFilterTabItem,
  DashboardTableEntryFields,
  FilterTabParam,
  getDefaultQueryForRole,
} from '@dv/shared/util/dashboard';

const resetTableFilterObj: Record<
  DashboardTableEntryFields | keyof SortAndPageInputs<unknown>,
  undefined
> = {
  fallNummer: undefined,
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

type DashboardTabItem = Omit<DashboardFilterTabItem, 'key'> & {
  key: FilterTabParam;
} & {
  roles: AvailableBenutzerRole[];
};
const baseFilterTabs = [
  {
    key: 'JURISTISCHE_ABKLAERUNG',
    route: ['gesuche'],
    roles: ['V0_Jurist'],
  },
  {
    key: 'ABKLAERUNG_DURCH_RECHSTABTEILUNG',
    route: ['gesuche'],
    roles: ['V0_Jurist'],
  },
  {
    key: 'ALLE',
    route: ['gesuche'],
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle', 'V0_Jurist'],
  },
  {
    key: 'AENDERUNGEN',
    route: ['gesuche'],
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle', 'V0_Jurist'],
  },
  {
    key: 'DRUCKBAR_VERFUEGUNGEN',
    route: ['gesuche'],
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    key: 'DRUCKBAR_DATENSCHUTZBRIEFE',
    route: ['gesuche'],
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    key: 'DARLEHEN',
    route: ['gesuche'],
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
  {
    key: 'FEHLGESCHLAGENE_ZAHLUNGEN',
    route: ['fehlgeschlagene-zahlungen'],
    class: 'tw:border-3! tw:[&]:border-dv-red!',
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle', 'V0_Jurist'],
  },
  {
    key: 'PENDENTE',
    route: ['gesuche'],
    class: 'tw:ml-auto',
    roles: ['V0_Sachbearbeiter', 'V0_Freigabestelle'],
  },
] satisfies Partial<DashboardTabItem>[];

@Component({
  selector: 'dv-sachbearbeitung-app-feature-dashboard',
  imports: [
    CommonModule,
    MatTabsModule,
    RouterModule,
    SachbearbeitungAppUiAdvTranslocoDirective,
  ],
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
    const filterTab =
      this.filterTab() ?? getDefaultQueryForRole(rolesMap).filterTab;
    const hasfehlgeschlageneZahlungen =
      this.fehlgeschlageneZahlungenStore.hasFehlgeschalgeneZahlungenSig();

    const tabs = baseFilterTabs.filter((tab) => {
      if (
        tab.key === 'FEHLGESCHLAGENE_ZAHLUNGEN' &&
        !hasfehlgeschlageneZahlungen &&
        // Do not hide the tab if user is already on given tab
        filterTab !== tab.key
      ) {
        return false;
      }

      return tab.roles.some((role) => rolesMap[role]);
    });

    return tabs.map(
      (tab) =>
        ({
          ...tab,
          active: tab.key === filterTab,
          // reset table filters when switching filter tabs
          queryParams: { ...resetTableFilterObj, filterTab: tab.key },
          queryParamsHandling: 'merge',
        }) satisfies DashboardTabItem,
    );
  });

  constructor() {
    // Load Fehlgeschlagene Zahlungen to check if there are any
    this.fehlgeschlageneZahlungenStore.getFehlgeschlageneZahlungen$({
      page: 1,
      pageSize: 1,
    });
  }
}
