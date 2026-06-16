import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  effect,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { FallStore } from '@dv/shared/data-access/fall';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { TRANCHE } from '@dv/shared/model/gesuch-form';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import {
  NavItem,
  buildDarlehenMenu,
  buildGesuchNavItems,
  createAllRouteParamsSig,
  createParamsIdSig,
  gesuchBaseMenuItems,
  getQueryParamValueSig,
} from '@dv/shared/util/navigation';

/**
 * Main layout for the gesuchsteller app.
 */
@Component({
  selector: 'dv-gesuch-app-pattern-main-layout',
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
export class GesuchAppPatternMainLayoutComponent {
  private fallStore = inject(FallStore);
  private darlehenStore = inject(DarlehenStore);
  private navigationStore = inject(NavigationStore);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);

  baseMenuItems = gesuchBaseMenuItems;

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  private allRouteParamsSig = createAllRouteParamsSig(this.router);

  private darlehenIdSig = createParamsIdSig(
    'darlehenId',
    this.allRouteParamsSig,
  );

  private originStepSig = getQueryParamValueSig(this.route, 'originStep');

  private gesuchIdSig = createParamsIdSig('gesuchId', this.allRouteParamsSig);

  private trancheIdSig = createParamsIdSig('trancheId', this.allRouteParamsSig);

  constructor() {
    this.fallStore.loadCurrentFall$();

    effect(() => {
      const fallId = this.fallStore.currentFallViewSig()?.id;

      if (fallId) {
        this.darlehenStore.getAllDarlehenGs$({ fallId });
      }
    });

    // navigation items effect
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      const darlehnen = this.darlehenStore.darlehenGsViewSig();
      const fallId = this.fallStore.currentFallViewSig()?.id;
      const darlehenId = this.darlehenIdSig();
      const rolesMap = this.permissionStore.rolesMapSig();
      const originStep = this.originStepSig();
      const gesuchHeader = this.gesuchHeaderStore.viewSig();

      if (!fallId) {
        this.navigationStore.setNavigationItems(gesuchBaseMenuItems);
        return;
      }

      const tab = decodeURI(originStep ?? '') || TRANCHE.route;
      const tabSegments = tab.split('/').filter(Boolean);

      const gesuchNav = buildGesuchNavItems(
        gesuchId,
        gesuchHeader.currentTranches ?? [],
        tabSegments,
        this.trancheIdSig(),
      );

      const auszahlungMenu: NavItem = {
        type: 'link',
        icon: 'payments',
        id: 'auszahlungen',
        label: { key: 'shared.menu.auszahlung' },
        route: ['/auszahlung', fallId],
      };

      const nachrichten: NavItem = {
        type: 'link',
        id: 'nachrichten',
        icon: 'mail',
        label: { key: 'shared.menu.nachrichten' },
        route: ['/nachrichten', fallId],
        badge: gesuchHeader.unreadNotificationsCount
          ? {
              count: gesuchHeader.unreadNotificationsCount,
              type: 'info',
            }
          : undefined,
      };

      const darlehenMenu = buildDarlehenMenu({
        darlehen: darlehnen.list,
        canCreateDarlehen: darlehnen.canCreateDarlehen,
        fallId: fallId,
        isDarlehenRoute: !!darlehenId,
        createDarlehen: () =>
          this.darlehenStore.createDarlehen$({
            fallId: fallId,
          }),
      });

      const navItems: NavItem[] = [
        ...gesuchBaseMenuItems,
        ...gesuchNav,
        darlehenMenu,
        auszahlungMenu,
        nachrichten,
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
