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
import { TRANCHE } from '@dv/shared/model/gesuch-form';
import {
  SharedPatternGlobalHeaderComponent,
  SharedPatternGlobalHeaderPartsDirective,
} from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import {
  NavItem,
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
    SharedPatternGlobalHeaderPartsDirective,
    SharedUiAdvTranslocoDirective,
    SharedUiTruncateTooltipDirective,
  ],
  template: `<mat-sidenav-container *dvTranslocoShared="let t">
    <mat-sidenav #sidenav mode="over" position="end">
      <dv-shared-pattern-mobile-sidenav (closeSidenav)="sidenav.close()">
      </dv-shared-pattern-mobile-sidenav>
    </mat-sidenav>
    <mat-sidenav-content class="tw:flex tw:flex-col">
      <dv-shared-pattern-global-header
        [staticNavItemsSig]="baseMenuItems"
        (closeSidenav)="sidenav.close()"
        (openSidenav)="sidenav.open()"
      >
        <div dvGlobalHeaderRight class="tw:min-w-0">
          @if (aktiveDelegierungSig(); as delegierung) {
            <div
              class="tw:dv-chip-info tw:ml-8"
              [dvTruncateTooltip]="delegierung.sozialdienst.name"
            >
              {{ t('shared.header.gesuch.istDelegiert') }}:
              {{ delegierung.sozialdienst.name }}
            </div>
          }
        </div>
      </dv-shared-pattern-global-header>

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

  private originStepSig = getQueryParamValueSig(this.route, 'originStep');

  private gesuchIdSig = createParamsIdSig('gesuchId', this.allRouteParamsSig);

  private trancheIdSig = createParamsIdSig('trancheId', this.allRouteParamsSig);

  private availableSozialdiensteSig = computed(() => {
    const sozialdienste = this.sozialdienstStore.availableSozialdienste()?.data;

    return sozialdienste?.filter((sozialdienst) => sozialdienst.aktiv);
  });

  aktiveDelegierungSig = computed(() =>
    this.fallHeaderStore.aktiveDelegierungSig(),
  );

  constructor() {
    this.fallStore.loadCurrentFall$();

    effect(() => {
      const fallId = this.fallStore.currentFallViewSig()?.id;

      if (fallId) {
        this.fallHeaderStore.loadFallHeader$({ fallId });
        this.sozialdienstStore.loadAvailableSozialdienste$();
      }
    });

    // navigationItems items effect
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      const fallId = this.fallStore.currentFallViewSig()?.id;
      const rolesMap = this.permissionStore.rolesMapSig();
      const originStep = this.originStepSig();
      const gesuchHeader = this.gesuchHeaderStore.viewSig();
      const fallHeader = this.fallHeaderStore.fallHeaderViewSig();
      const availableSozialdienste = this.availableSozialdiensteSig() ?? [];
      const currentDelegierung =
        this.fallHeaderStore.cachedFallHeader().data?.currentDelegierung;

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

      const sozialdienstMenu: NavItem | undefined =
        availableSozialdienste.length && !currentDelegierung
          ? {
              type: 'action',
              id: 'sozialdienst-delegieren',
              label: { key: 'shared.menu.delegieren' },
              action: () => this.delegiereSozialdienst(fallId),
            }
          : undefined;

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

      const navItems: NavItem[] = [
        ...gesuchBaseMenuItems,
        ...gesuchNav,
        ...(sozialdienstMenu ? [sozialdienstMenu] : []),
        fallDokumente,
        auszahlung,
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

  private delegiereSozialdienst(fallId: string) {
    GesuchAppDialogDelegierenComponent.open(this.dialog)
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.sozialdienstStore.fallDelegieren$({
            req: {
              sozialdienstId: result.sozialdienstId,
              fallId,
              delegierungCreate: result.persoenlicheAngaben,
            },
            onSuccess: () => {
              this.globalNotificationStore.createSuccessNotification({
                messageKey: 'shared.dashboard.gesuch.delegieren.success',
              });
              const fallId = this.fallStore.currentFallViewSig()?.id;
              if (fallId) {
                this.fallHeaderStore.loadFallHeader$({ fallId });
              }
            },
          });
        }
      });
  }
}
