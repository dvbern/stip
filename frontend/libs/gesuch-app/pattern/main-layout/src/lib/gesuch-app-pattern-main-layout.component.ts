import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  effect,
  inject,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSidenavModule } from '@angular/material/sidenav';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';

import { GesuchAppDialogDelegierenComponent } from '@dv/gesuch-app/dialog/delegieren';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { FallStore } from '@dv/shared/data-access/fall';
import { FallHeaderStore } from '@dv/shared/data-access/fall-header';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { PermissionStore } from '@dv/shared/global/permission';
import { SozialdienstSlim } from '@dv/shared/model/gesuch';
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
  providers: [SozialdienstStore],
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
  private dialog = inject(MatDialog);
  private darlehenStore = inject(DarlehenStore);
  // private dashboardStore = inject(DashboardStore);
  private navigationStore = inject(NavigationStore);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);
  private fallHeaderStore = inject(FallHeaderStore);
  private sozialdienstStore = inject(SozialdienstStore);
  private globalNotificationStore = inject(GlobalNotificationStore);

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

  // toto: if delegiert, only show delegierung, not menu, impl after dto change
  private availableSozialdiensteSig = computed(() => {
    const sozialdienste = this.sozialdienstStore.availableSozialdienste()?.data;
    // const delegierterSozialdienst =
    //   this.dashboardStore.dashboardViewSig()?.currentDelegierung?.sozialdienst;

    // return sozialdienste?.filter(
    //   (sozialdienst) =>
    //     sozialdienst.aktiv || sozialdienst.id === delegierterSozialdienst?.id,
    // );
    return sozialdienste;
  });

  constructor() {
    this.fallStore.loadCurrentFall$();

    effect(() => {
      const fallId = this.fallStore.currentFallViewSig()?.id;

      if (fallId) {
        this.darlehenStore.getAllDarlehenGs$({ fallId });
        this.fallHeaderStore.loadFallHeader$({ fallId });
        // this.dashboardStore.loadDashboard$(); // todo: after change dto
        this.sozialdienstStore.loadAvailableSozialdienste$();
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
      const fallHeader = this.fallHeaderStore.fallHeaderViewSig();
      // todo: after change dto
      // const dashboardView = this.dashboardStore.dashboardViewSig();
      // const delegierterSozialdienst =
      //   dashboardView?.currentDelegierung?.sozialdienst;
      const availableSozialdienste = this.availableSozialdiensteSig() ?? [];

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

      const auszahlung: NavItem = {
        type: 'link',
        icon: 'payments',
        id: 'auszahlungen',
        label: { key: 'shared.menu.auszahlung' },
        route: ['/auszahlung', fallId],
      };

      const fallDokumente: NavItem = {
        type: 'link',
        id: 'fall-dokumente',
        icon: 'description',
        label: { key: 'shared.menu.fallDokumente' },
        route: ['/fall-dokumente', fallId],
      };

      const nachrichten: NavItem = {
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

      // todo: to be moved into right menu!
      const sozialdienstMenu: NavItem | undefined =
        availableSozialdienste.length
          ? {
              type: 'menu',
              id: 'sozialdienst-delegieren',
              icon: 'interpreter_mode',
              label: { key: 'shared.dashboard.gesuch.sozialdienst' },
              children: availableSozialdienste.map((sozialdienst) => ({
                type: 'action' as const,
                id: `sozialdienst-delegieren-${sozialdienst.id}`,
                label: {
                  key: sozialdienst.name,
                },
                icon: 'arrow_forward',
                disabled: !sozialdienst.aktiv,
                action: () => this.delegiereSozialdienst(fallId, sozialdienst),
              })),
            }
          : undefined;

      const navItems: NavItem[] = [
        ...gesuchBaseMenuItems,
        ...gesuchNav,
        darlehenMenu,
        fallDokumente,
        auszahlung,
        nachrichten,
        ...(sozialdienstMenu ? [sozialdienstMenu] : []),
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

  private delegiereSozialdienst(
    fallId: string,
    sozialdienst: SozialdienstSlim,
  ) {
    GesuchAppDialogDelegierenComponent.open(this.dialog)
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.sozialdienstStore.fallDelegieren$({
            req: {
              sozialdienstId: sozialdienst.id,
              fallId,
              delegierungCreate: result,
            },
            onSuccess: () => {
              this.globalNotificationStore.createSuccessNotification({
                messageKey: 'shared.dashboard.gesuch.delegieren.success',
              });
              // this.dashboardStore.loadDashboard$(); // todo: after change dto
            },
          });
        }
      });
  }
}
