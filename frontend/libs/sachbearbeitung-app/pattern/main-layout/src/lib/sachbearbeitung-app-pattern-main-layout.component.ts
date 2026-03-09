import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatSidenavModule } from '@angular/material/sidenav';
import {
  ActivatedRoute,
  NavigationEnd,
  Router,
  RouterOutlet,
} from '@angular/router';
import { filter, map } from 'rxjs';

import { FehlgeschlageneZahlungenStore } from '@dv/sachbearbeitung-app/data-access/fehlgeschlagene-zahlungen';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { FallStore } from '@dv/shared/data-access/fall';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { DarlehenStatus } from '@dv/shared/model/gesuch';
import { NavItem } from '@dv/shared/model/ui';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';

// todo: really needed?
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
        [staticNavItems]="navItemsSig()"
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
  private fallStore = inject(FallStore);
  private darlehenStore = inject(DarlehenStore);
  private navigationStore = inject(NavigationStore);
  private permissionStore = inject(PermissionStore);
  private router = inject(Router);

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  // todo: needed?
  private allRouteParamsSig = toSignal(
    this.router.events.pipe(
      filter((event) => event instanceof NavigationEnd),
      map(() => {
        let route: ActivatedRoute | null = this.router.routerState.root;
        const params: Record<string, string> = {};

        while (route) {
          Object.assign(params, route.snapshot.params);
          route = route.firstChild;
        }

        return params;
      }),
    ),
  );

  fehlgeschlageneZahlungenStore = inject(FehlgeschlageneZahlungenStore);

  navItemsSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();

    const navItems: NavItem[] = baseNavItems;

    // todo: test
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
