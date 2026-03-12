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

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { FallStore } from '@dv/shared/data-access/fall';
import { selectSharedDataAccessGesuchCache } from '@dv/shared/data-access/gesuch';
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
export class SozialdienstAppPatternMainLayoutComponent {
  private fallStore = inject(FallStore);
  private darlehenStore = inject(DarlehenStore);
  private navigationStore = inject(NavigationStore);
  private router = inject(Router);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);
  private store = inject(Store);

  baseMenuItems = sozialdienstBaseMenuItems;

  cacheSig = this.store.selectSignal(selectSharedDataAccessGesuchCache);

  fallIdFromGesuchCacheSig = computed(() => {
    const { gesuch } = this.cacheSig();
    return gesuch?.fallId;
  });

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  private allRouteParamsSig = createAllRouteParamsSig(this.router);

  private darlehenIdSig = createParamsIdSig(
    'darlehenId',
    this.allRouteParamsSig,
  );

  private gesuchIdSig = createParamsIdSig('gesuchId', this.allRouteParamsSig);

  private trancheIdSig = createParamsIdSig('trancheId', this.allRouteParamsSig);

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
      const fallId = this.fallIdSig();
      if (fallId) {
        this.darlehenStore.getAllDarlehenGs$({ fallId });
      }
    });

    // naviation items effect
    effect(() => {
      const darlehnen = this.darlehenStore.darlehenGsViewSig();
      const fallId = this.fallIdSig();
      const gesuchId = this.gesuchIdSig();
      const darlehenId = this.darlehenIdSig();
      const rolesMap = this.permissionStore.rolesMapSig();

      if (!fallId) {
        this.navigationStore.setNavigationItems(sozialdienstBaseMenuItems);
        return;
      }

      const fallNav: NavItem[] = [];
      const ausZahlungNav: NavItem[] = [];

      if (fallId) {
        fallNav.push({
          type: 'link',
          id: 'fall',
          label: { key: 'sozialdienst-app.header.fall' },
          icon: 'assignment_ind',
          route: ['/fall', fallId],
        });

        ausZahlungNav.push(
          {
            type: 'link',
            id: 'auszahlung',
            label: { key: 'sozialdienst-app.header.auszahlung' },
            icon: 'payments',
            route: ['/auszahlung', fallId],
          },
          {
            type: 'separator',
            id: 'separator-1',
            orientation: 'vertical',
          },
        );
      }

      const gesuchNav = buildGesuchNavItems(
        gesuchId,
        this.gesuchHeaderStore.viewGsSig().currentTranchen ?? [],
        'sozialdienst-app',
      );

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
        ...fallNav,
        ...gesuchNav,
        darlehenMenu,
        ...ausZahlungNav,
        ...this.baseMenuItems,
      ].filter((item) => {
        if (!item.rolesAllowed || item.rolesAllowed.length === 0) {
          return true;
        }

        return item.rolesAllowed.some((role) => rolesMap[role]);
      });

      this.navigationStore.setNavigationItems(navItems);
    });

    effect(() => {
      const gesuchTrancheId = this.trancheIdSig();
      if (gesuchTrancheId) {
        this.gesuchHeaderStore.loadHeaderGs$({ gesuchTrancheId });
      }
    });
  }
}
