import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  effect,
  inject,
  input,
  untracked,
  viewChild,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
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
import { NavItem, NavigationStore } from '@dv/shared/data-access/navigation';
import { DarlehenStatus } from '@dv/shared/model/gesuch';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';

const gsBaseMenuItems: NavItem[] = [
  {
    type: 'link',
    id: 'dashboard',
    icon: 'dashboard',
    label: 'Dashboard',
    route: ['/dashboard'],
  },
];

type DarlehenCompleteStates = 'open' | 'rejected' | 'accepted';
const darlehenStatusMapping: Record<DarlehenStatus, DarlehenCompleteStates> = {
  IN_BEARBEITUNG_GS: 'open',
  EINGEGEBEN: 'open',
  IN_FREIGABE: 'open',
  ABGELEHNT: 'rejected',
  AKZEPTIERT: 'accepted',
};

const darlehenCompletedStates: DarlehenCompleteStates[] = [
  'open',
  'rejected',
  'accepted',
];

@Component({
  selector: 'dv-shared-pattern-gesuchsteller-layout',
  imports: [
    MatSidenavModule,
    RouterOutlet,
    SharedPatternMobileSidenavComponent,
    SharedPatternGlobalHeaderComponent,
  ],
  template: `<mat-sidenav-container>
    <mat-sidenav #sidenav mode="over" position="end">
      <dv-shared-pattern-mobile-sidenav (closeSidenav)="sidenav.close()">
        <ng-content
          select="[dvMobileNavContent]"
          ngProjectAs="[dvMobileNavContent]"
        ></ng-content>
      </dv-shared-pattern-mobile-sidenav>
    </mat-sidenav>
    <mat-sidenav-content class="d-flex flex-column">
      <dv-shared-pattern-global-header
        [staticNavItems]="staticNavItems"
        (closeSidenav)="sidenav.close()"
        (openSidenav)="sidenav.open()"
      ></dv-shared-pattern-global-header>

      <main class="page-body">
        <router-outlet></router-outlet>
      </main>
    </mat-sidenav-content>
  </mat-sidenav-container>`,
  styles: ``,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternGesuchstellerLayoutComponent {
  private sidenavSig = viewChild.required(MatSidenav);
  // todo: refactor
  // eslint-disable-next-line @angular-eslint/no-input-rename
  closeMenuSig = input<{ value: boolean } | null>(null, { alias: 'closeMenu' });

  private fallStore = inject(FallStore);
  private darlehenStore = inject(DarlehenStore);
  private navigationStore = inject(NavigationStore);
  private router = inject(Router);

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
    console.log('Current route params:', params);
    return params?.['darlehenId'] ? true : false;
  });

  constructor() {
    this.fallStore.loadCurrentFall$();

    effect(() => {
      if (this.closeMenuSig()?.value) {
        this.sidenavSig().close();
      }
    });

    effect(() => {
      const fallId = this.fallStore.currentFallViewSig()?.id;

      if (fallId) {
        this.darlehenStore.getAllDarlehenGs$({ fallId });
      }
    });

    effect(() => {
      const darlehnen = this.darlehenStore.darlehenGsViewSig();
      const fallId = untracked(this.fallStore.currentFallViewSig)?.id ?? '';
      const isDarlehenRoute = this.isDarlehenRouteSig();

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
              label: status.toUpperCase(),
            });

            items.push(
              ...darlehen.map((darlehen) => ({
                type: 'link' as const,
                id: darlehen.id,
                // todo: translate
                label: `Darlehen vom ${format(darlehen.timestampErstellt!, 'dd.MM.yyyy')}`,
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
        label: 'Darlehen',
        children: darlehnen.canCreateDarlehen
          ? darlehenMenuItems.concat([
              {
                type: 'separator',
                id: 'separator-create',
                label: '',
              },
              {
                type: 'action',
                id: 'create-darlehen',
                label: 'Neues Darlehen',
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
        label: 'Auszahlung',
        route: ['/auszahlung', fallId],
      };

      const navItems: NavItem[] = [
        ...gsBaseMenuItems,
        darlehenMenu,
        auszahlungMenu,
      ];

      this.navigationStore.setNavigationItems(navItems);
    });
  }

  staticNavItems: NavItem[] = [
    {
      type: 'link',
      id: 'dashboard',
      label: 'Dashboard',
      icon: 'dashboard',
      route: ['/dashboard'],
    },
  ];
}
