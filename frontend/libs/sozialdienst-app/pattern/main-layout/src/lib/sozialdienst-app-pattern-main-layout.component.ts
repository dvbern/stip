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
import { format } from 'date-fns/format';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { FallStore } from '@dv/shared/data-access/fall';
import { selectSharedDataAccessGesuchCache } from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  darlehenCompletedStates,
  darlehenStatusMapping,
} from '@dv/shared/model/ui';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import {
  NavItem,
  createAllRouteParamsSig,
  createParamsIdSig,
} from '@dv/shared/util/navigation';

const sozialdienstBaseMenuItems: NavItem[] = [
  {
    type: 'link',
    id: 'antraege',
    label: { key: 'sozialdienst-app.header.antraege' },
    icon: 'list',
    route: ['/dashboard'],
  },
  {
    type: 'link',
    id: 'administration',
    label: { key: 'sozialdienst-app.header.administration' },
    icon: 'settings',
    route: ['/administration'],
    rolesAllowed: ['V0_Sozialdienst-Admin'],
  },
];

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
  private config = inject(SharedModelCompileTimeConfig);

  cacheSig = this.store.selectSignal(selectSharedDataAccessGesuchCache);

  fallIdFromGesuchCacheSig = computed(() => {
    const { gesuch } = this.cacheSig();
    return gesuch?.fallId;
  });

  baseMenuItems = sozialdienstBaseMenuItems;

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

  private currentBenutzerFallIdSig = this.fallStore.currentFallViewSig;

  private fallIdSig = computed(() => {
    const appType = this.config.appType;

    if (appType === 'sozialdienst-app') {
      return this.routeParamsFallIdSig() ?? this.fallIdFromGesuchCacheSig();
    }
    if (appType === 'gesuch-app') {
      return this.currentBenutzerFallIdSig()?.id;
    }

    return undefined;
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
      const fallId = this.fallIdSig() ?? this.fallIdFromGesuchCacheSig() ?? '';
      const gesuchId = this.gesuchIdSig();
      const darlehenId = this.darlehenIdSig();
      const rolesMap = this.permissionStore.rolesMapSig();

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

      const gesuchNav: NavItem[] = [];

      if (gesuchId) {
        const tranchen =
          this.gesuchHeaderStore.viewGsSig().currentTranchen ?? [];

        if (tranchen.length > 1) {
          gesuchNav.push({
            type: 'menu',
            id: 'gesuch',
            label: { key: 'sozialdienst-app.header.gesuch' },
            icon: 'description',
            children: tranchen.map((tranche, index) => ({
              type: 'link' as const,
              id: tranche.id,
              label: {
                key: 'shared.header.tranche.item',
                context: {
                  date: format(tranche.gueltigAb, 'dd.MM.yyyy'),
                  index: index + 1,
                },
              },
              route: ['/gesuch', gesuchId, 'tranche', tranche.id],
            })),
          });
        } else if (tranchen.length === 1) {
          gesuchNav.push({
            type: 'link',
            id: 'gesuch',
            label: { key: 'sozialdienst-app.header.gesuch' },
            icon: 'description',
            route: ['/gesuch', gesuchId, 'tranche', tranchen[0].id],
            active: !!gesuchId,
          });
        }
      }

      const darlehenListByStatus = darlehenCompletedStates.map((status) => ({
        status,
        darlehen: darlehnen.list.filter(
          (dar) => darlehenStatusMapping[dar.status!] === status,
        ),
      }));

      // list with separators for each status
      const darlehenMenuItems: NavItem[] = darlehenListByStatus.flatMap(
        ({ status, darlehen }) => {
          const items: NavItem[] = [];

          if (darlehen.length > 0) {
            items.push({
              type: 'separator',
              id: `separator-${status}`,
              label: {
                key: 'shared.header.darlehen.complete-states.' + status,
              },
            });

            items.push(
              ...darlehen.map((darlehen) => ({
                type: 'link' as const,
                id: darlehen.id,
                label: {
                  key: 'shared.header.darlehen.item',
                  context: {
                    date: format(darlehen.timestampErstellt!, 'dd.MM.yyyy'),
                  },
                },
                route: ['/darlehen', darlehen.id, 'fall', fallId],
              })),
            );
          }

          return items;
        },
      );

      const darlehenMenu: NavItem = {
        type: 'menu',
        icon: 'account_balance',
        id: 'darlehen',
        label: { key: 'shared.header.darlehen' },
        children: darlehnen.canCreateDarlehen
          ? darlehenMenuItems.concat([
              ...(darlehenMenuItems.length > 0
                ? [
                    {
                      type: 'separator' as const,
                      id: 'separator-create',
                    },
                  ]
                : []),
              {
                type: 'action',
                id: 'create-darlehen',
                label: { key: 'shared.header.darlehen.create' },
                icon: 'add',
                action: () =>
                  this.darlehenStore.createDarlehen$({
                    fallId,
                  }),
              },
            ])
          : darlehenMenuItems,
        active: !!darlehenId,
      };

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
