import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
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
import { NavItem, NavigationStore } from '@dv/shared/data-access/navigation';
import { DarlehenStatus } from '@dv/shared/model/gesuch';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';

const gsBaseMenuItems: NavItem[] = [
  {
    type: 'link',
    id: 'dashboard',
    icon: 'dashboard',
    label: { key: 'gesuch-app.dashboard.title' },
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
        [staticNavItems]="staticNavItems"
        (closeSidenav)="sidenav.close()"
        (openSidenav)="sidenav.open()"
      ></dv-shared-pattern-global-header>

      <main class="page-body">
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
  private router = inject(Router);

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  fehlgeschlageneZahlungenStore = inject(FehlgeschlageneZahlungenStore);

  // todo: also show in header!
  constructor() {
    this.fehlgeschlageneZahlungenStore.getFehlgeschlageneZahlungen$({
      page: 1,
      pageSize: 10,
    });
  }

  // @if (fehlgeschlageneZahlungenStore.hasFehlgeschalgeneZahlungenSig()) {
  //         <a
  //           routerLink="/fehlgeschlagene-zahlungen"
  //           routerLinkActive="active"
  //           class="btn btn-nav fw-normal px-2 d-flex align-items-center shadow-none"
  //           data-testid="gesuch-step-nav-fehlgeschlagene-zahlungen"
  //         >
  //           <i class="material-symbols-rounded text-white me-2">error_outline</i>
  //           <span>
  //             {{ 'sachbearbeitung-app.header.fehlgeschlageneZahlungen' | transloco }}
  //           </span>
  //         </a>
  //       }

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

  // todo: really needed?
  // Anträge, Darlehen-Dashboard (until rework), Massendruck, Administration, Fehlgeschlagene Zahlungen
  staticNavItems: NavItem[] = [
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
    },
  ];
}
