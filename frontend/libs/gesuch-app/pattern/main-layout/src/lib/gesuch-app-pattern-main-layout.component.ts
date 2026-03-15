import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  effect,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterOutlet } from '@angular/router';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { FallStore } from '@dv/shared/data-access/fall';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import {
  NavItem,
  buildDarlehenMenu,
  buildGesuchNavItems,
  createAllRouteParamsSig,
  createParamsIdSig,
  gesuchBaseMenuItems,
} from '@dv/shared/util/navigation';

/**
 * Main layout for the gesuchsteller app.
 * todo-before-merge: make or share with sozialdienst since it is mostly the same for all apps?
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
    <mat-sidenav-content class="d-flex flex-column">
      <dv-shared-pattern-global-header
        [staticNavItemsSig]="baseMenuItems"
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
export class GesuchAppPatternMainLayoutComponent {
  private fallStore = inject(FallStore);
  private darlehenStore = inject(DarlehenStore);
  private navigationStore = inject(NavigationStore);
  private router = inject(Router);
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

    // naviation items effect
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      const darlehnen = this.darlehenStore.darlehenGsViewSig();
      const fallId = this.fallStore.currentFallViewSig()?.id;
      const darlehenId = this.darlehenIdSig();
      const rolesMap = this.permissionStore.rolesMapSig();

      if (!fallId) {
        this.navigationStore.setNavigationItems(gesuchBaseMenuItems);
        return;
      }

      const gesuchNav = buildGesuchNavItems(
        gesuchId,
        this.gesuchHeaderStore.viewSig().currentTranches ?? [],
        this.trancheIdSig(),
      );

      const auszahlungMenu: NavItem = {
        type: 'link',
        icon: 'payments',
        id: 'auszahlungen',
        label: { key: 'shared.menu.auszahlung' },
        route: ['/auszahlung', fallId],
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
