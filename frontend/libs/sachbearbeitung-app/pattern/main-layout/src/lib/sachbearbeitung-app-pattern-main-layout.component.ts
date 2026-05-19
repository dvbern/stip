import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterOutlet } from '@angular/router';
import { map, startWith } from 'rxjs';

import { PermissionStore } from '@dv/shared/global/permission';
import { urlAfterNavigationEnd } from '@dv/shared/model/router';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import {
  NullableDashFilterQueryParams,
  getDefaultQueryForRole,
} from '@dv/shared/util/dashboard';
import { NavItem } from '@dv/shared/util/navigation';

const baseNavItems: (NavItem & {
  queryParams?: NullableDashFilterQueryParams;
})[] = [
  {
    type: 'link',
    id: 'dashboard',
    label: { key: 'sachbearbeitung-app.header.antraege' },
    icon: 'dashboard',
    route: ['/dashboard', 'antraege'],
    testId: 'dashboard-nav-item',
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
    rolesAllowed: ['V0_Sachbearbeiter-Admin', 'V0_Jurist'],
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
    <mat-sidenav-content class="tw:flex tw:flex-col">
      <dv-shared-pattern-global-header
        [staticNavItemsSig]="navItemsSig()"
        (closeSidenav)="sidenav.close()"
        (openSidenav)="sidenav.open()"
      ></dv-shared-pattern-global-header>

      <main class="tw:dv-page-body tw:flex tw:flex-col">
        <router-outlet></router-outlet>
      </main>
    </mat-sidenav-content>
  </mat-sidenav-container>`,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternMainLayoutComponent {
  private permissionStore = inject(PermissionStore);
  private router = inject(Router);

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  routeUrlSig = toSignal(
    urlAfterNavigationEnd(this.router).pipe(
      map(() => this.router.routerState.snapshot.url),
      startWith(this.router.routerState.snapshot.url),
    ),
  );

  navItemsSig = computed(() => {
    const rolesMap = this.permissionStore.rolesMapSig();

    const defaultFilter = getDefaultQueryForRole(rolesMap);

    const navItems: NavItem[] = baseNavItems;

    const filtered: NavItem[] = navItems
      .filter((item) => {
        if (!item.rolesAllowed || item.rolesAllowed.length === 0) {
          return true;
        }

        return item.rolesAllowed.some((role) => rolesMap[role]);
      })
      .map((item) => {
        if (item.type === 'link' && item.route) {
          const isActive = this.routeUrlSig()?.includes(item.route[0] ?? '');
          return { ...item, active: isActive };
        }

        if (item.id === 'dashboard') {
          return {
            ...item,
            route: ['/dashboard', 'antraege'],
            queryParams: {
              filterTab: defaultFilter.filterTab,
              scope: defaultFilter.zugewiesen,
              bearbeitbar: defaultFilter.bearbeitbar,
            },
          };
        }

        return item;
      });

    return filtered;
  });
}
