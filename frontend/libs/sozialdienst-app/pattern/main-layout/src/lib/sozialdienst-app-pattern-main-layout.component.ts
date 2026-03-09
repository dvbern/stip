import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  effect,
  inject,
  untracked,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatSidenavModule } from '@angular/material/sidenav';
import {
  ActivatedRoute,
  NavigationEnd,
  Router,
  RouterOutlet,
} from '@angular/router';
import { format } from 'date-fns/format';
import { filter, map } from 'rxjs';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { FallStore } from '@dv/shared/data-access/fall';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { PermissionStore } from '@dv/shared/global/permission';
import {
  NavItem,
  darlehenCompletedStates,
  darlehenStatusMapping,
} from '@dv/shared/model/ui';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';

const sozialdienstBaseMenuItems: NavItem[] = [
  {
    type: 'link',
    id: 'antraege',
    label: { key: 'sozialdienst-app.header.antraege' },
    icon: 'list',
    route: ['/'],
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
 * This is the new main layout for the sozialdienst app.
 * This will also change once we have the new design to what SB is going to be.
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
  // todo: dynamic nav items on fall route: Antraege, Fall, Darlehen, Auszahlung, Administration
  private fallStore = inject(FallStore);
  private darlehenStore = inject(DarlehenStore);
  private navigationStore = inject(NavigationStore);
  private router = inject(Router);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);

  baseMenuItems = sozialdienstBaseMenuItems;

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

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

  private isDarlehenRouteSig = computed(() => {
    const params = this.allRouteParamsSig();
    return params?.['darlehenId'] ? true : false;
  });

  private gesuchIdSig = computed(() => {
    const params = this.allRouteParamsSig();
    return params?.['gesuchId'];
  });

  private fallIdSig = computed(() => {
    const params = this.allRouteParamsSig();
    return params?.['fallId'];
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
      const fallId = untracked(this.fallIdSig) ?? ''; // check if really ok with untracked and fallback!
      const gesuchId = this.gesuchIdSig();
      const isDarlehenRoute = this.isDarlehenRouteSig();
      const rolesMap = this.permissionStore.rolesMapSig();
      const gesuchNav: NavItem[] = [];

      if (gesuchId) {
        const tranchen =
          this.gesuchHeaderStore.viewGsSig().currentTranchen ?? [];

        // todo-before-merge or should we use this?
        // tranchenSig = this.gesuchHeaderStore.getRelativeTranchenViewGsSig(
        //   this.gesuchIdSig,
        // );

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
              route: [gesuchId, 'tranche', tranche.id],
            })),
          });
        } else if (tranchen.length === 1) {
          gesuchNav.push({
            type: 'link',
            id: 'gesuch',
            label: { key: 'sozialdienst-app.header.gesuch' },
            icon: 'description',
            route: [gesuchId, 'tranche', tranchen[0].id],
          });
        }
      }

      // todo: put into lib
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
        active: isDarlehenRoute,
      };

      const auszahlungMenu: NavItem = {
        type: 'link',
        icon: 'payments',
        id: 'auszahlungen',
        label: { key: 'shared.menu.auszahlung' },
        route: ['/auszahlung', fallId],
      };

      const navItems: NavItem[] = [
        ...this.baseMenuItems,
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
  }
}
