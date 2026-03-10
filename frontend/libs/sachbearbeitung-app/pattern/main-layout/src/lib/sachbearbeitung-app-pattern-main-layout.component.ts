import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterOutlet } from '@angular/router';

import { FehlgeschlageneZahlungenStore } from '@dv/sachbearbeitung-app/data-access/fehlgeschlagene-zahlungen';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { NavItem } from '@dv/shared/util/navigation';

// Anträge, Darlehen-Dashboard (until rework), Massendruck, Administration, Fehlgeschlagene Zahlungen
const baseNavItems: NavItem[] = [
  {
    type: 'link',
    id: 'dashboard',
    label: { key: 'sachbearbeitung-app.header.antraege' },
    icon: 'dashboard',
    route: ['/dashboard'],
  },
  {
    type: 'link',
    id: 'darlehen-dashboard',
    label: { key: 'sachbearbeitung-app.header.darlehen' },
    icon: 'payments',
    route: ['/darlehen-dashboard'],
  },
  {
    type: 'link',
    id: 'massendruck',
    label: { key: 'sachbearbeitung-app.header.massendruck' },
    icon: 'print',
    route: ['/massendruck'],
  },
  {
    type: 'link',
    id: 'administration',
    label: { key: 'sachbearbeitung-app.header.administration' },
    icon: 'settings',
    route: ['/administration'],
    rolesAllowed: ['V0_Sachbearbeiter-Admin'],
  },
];

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-main-layout',
  imports: [
    MatSidenavModule,
    RouterOutlet,
    SharedPatternMobileSidenavComponent,
    SharedPatternGlobalHeaderComponent,
  ],
  template: `<mat-sidenav-container>
    <mat-sidenav #sidenav mode="over" position="end">
      <dv-shared-pattern-mobile-sidenav (closeSidenav)="sidenav.close()">
      </dv-shared-pattern-mobile-sidenav>
    </mat-sidenav>
    <mat-sidenav-content class="d-flex flex-column">
      <dv-shared-pattern-global-header
        [staticNavItemsSig]="navItemsSig()"
        (closeSidenav)="sidenav.close()"
        (openSidenav)="sidenav.open()"
      ></dv-shared-pattern-global-header>

      <main class="page-body tw:flex tw:flex-col">
        <router-outlet></router-outlet>
      </main>
    </mat-sidenav-content>
  </mat-sidenav-container>`,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternMainLayoutComponent {
  private permissionStore = inject(PermissionStore);

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  fehlgeschlageneZahlungenStore = inject(FehlgeschlageneZahlungenStore);

  navItemsSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();

    const navItems: NavItem[] = baseNavItems;

    // todo-after-merge: test
    if (this.fehlgeschlageneZahlungenStore.hasFehlgeschalgeneZahlungenSig()) {
      navItems.push({
        type: 'link',
        id: 'fehlgeschlagene-zahlungen',
        label: { key: 'sachbearbeitung-app.header.fehlgeschlageneZahlungen' },
        icon: 'error_outline',
        route: ['/fehlgeschlagene-zahlungen'],
      });
    }

    const filtered: NavItem[] = navItems.filter((item) => {
      if (!item.rolesAllowed || item.rolesAllowed.length === 0) {
        return true;
      }

      return item.rolesAllowed.some((role) => rolesMap[role]);
    });

    return filtered;
  });

  constructor() {
    this.fehlgeschlageneZahlungenStore.getFehlgeschlageneZahlungen$({
      page: 1,
      pageSize: 10,
    });
  }
}
