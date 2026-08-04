import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterOutlet } from '@angular/router';
import { Store } from '@ngrx/store';

import { FallStore } from '@dv/shared/data-access/fall';
import { FallHeaderStore } from '@dv/shared/data-access/fall-header';
import { selectSharedDataAccessGesuchCache } from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import {
  NavItem,
  createAllRouteParamsSig,
  createParamsIdSig,
  sozialdienstAdminMenuItems,
  sozialdienstBaseMenuItems,
} from '@dv/shared/util/navigation';

/**
 * Main layout for the sozialdienst app.
 * This will also change once we have the new design to what SB is going to be.
 * In the Sozialdienst app, the fallId has to be the fallId of the GS, not the fall of the soz-mitarbeiter!
 */
@Component({
  selector: 'dv-sozialdienst-app-pattern-main-layout',
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
        [staticNavItemsSig]="baseMenuItems"
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
export class SozialdienstAppPatternMainLayoutComponent {
  private fallStore = inject(FallStore);
  private navigationStore = inject(NavigationStore);
  private router = inject(Router);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);
  private store = inject(Store);
  private fallHeaderStore = inject(FallHeaderStore);

  baseMenuItems = sozialdienstBaseMenuItems;

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  cacheSig = this.store.selectSignal(selectSharedDataAccessGesuchCache);

  fallIdFromGesuchCacheSig = computed(() => {
    const { gesuch } = this.cacheSig();
    return gesuch?.fallId;
  });

  private allRouteParamsSig = createAllRouteParamsSig(this.router);

  private gesuchIdSig = createParamsIdSig('gesuchId', this.allRouteParamsSig);

  private routeParamsFallIdSig = createParamsIdSig(
    'fallId',
    this.allRouteParamsSig,
  );

  private fallIdSig = computed(() => {
    const routeFallId = this.routeParamsFallIdSig();
    const cacheFallId = this.fallIdFromGesuchCacheSig();
    return routeFallId ?? cacheFallId;
  });

  constructor() {
    this.fallStore.loadCurrentFall$();

    effect(() => {
      // Read allRouteParamsSig to re-run on every navigation
      this.allRouteParamsSig();
      const fallId = this.fallIdSig();
      if (fallId) {
        this.fallHeaderStore.loadFallHeader$({ fallId });
      }
    });

    // navigation items effect
    effect(() => {
      // Read allRouteParamsSig to re-run on every navigation
      this.allRouteParamsSig();
      const fallId = this.fallIdSig();
      const rolesMap = this.permissionStore.rolesMapSig();
      const fallHeader = this.fallHeaderStore.fallHeaderViewSig();

      if (!fallId) {
        this.navigationStore.setNavigationItems([
          ...sozialdienstBaseMenuItems,
          ...sozialdienstAdminMenuItems,
        ]);
        return;
      }

      const fallNav: NavItem = {
        type: 'link',
        id: 'fall',
        label: { key: 'shared.header.fall' },
        icon: 'assignment_ind',
        route: ['/fall', fallId],
      };

      const auszahlung: NavItem = {
        type: 'link',
        id: 'auszahlung',
        label: { key: 'shared.header.auszahlung' },
        icon: 'payments',
        route: ['/auszahlung', fallId],
      };

      const fallDokumente: NavItem = {
        type: 'link',
        id: 'fall-dokumente',
        label: { key: 'shared.menu.fallDokumente' },
        icon: 'description',
        route: ['/fall-dokumente', fallId],
      };

      const nachrichten: NavItem[] = [
        {
          type: 'link',
          id: 'nachrichten',
          icon: 'mail',
          label: { key: 'shared.menu.nachrichten' },
          route: ['/nachrichten', fallId],
          badge: fallHeader?.unreadNotificationsCount
            ? {
                count: fallHeader.unreadNotificationsCount,
              }
            : undefined,
        },
        {
          type: 'separator',
          id: 'separator-1',
          orientation: 'vertical',
        },
      ];

      const navItems: NavItem[] = [
        fallNav,
        fallDokumente,
        auszahlung,
        ...nachrichten,
        ...sozialdienstBaseMenuItems,
      ].filter((item) => {
        if (!item.rolesAllowed || item.rolesAllowed.length === 0) {
          return true;
        }

        return item.rolesAllowed.some((role) => rolesMap[role]);
      });

      this.navigationStore.setNavigationItems(navItems);
    });

    effect(() => {
      const gesuchId = this.gesuchIdSig();
      if (gesuchId) {
        this.gesuchHeaderStore.loadHeader$({ gesuchId });
      }
    });
  }
}
