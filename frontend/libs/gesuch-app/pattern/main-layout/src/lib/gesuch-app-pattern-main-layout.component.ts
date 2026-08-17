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
import { Router, RouterOutlet } from '@angular/router';
import { Store } from '@ngrx/store';

import { GesuchAppDialogDelegierenComponent } from '@dv/gesuch-app/dialog/delegieren';
import {
  SharedDataAccessBenutzerApiEvents,
  selectSharedDataAccessBenutzer,
} from '@dv/shared/data-access/benutzer';
import { FallStore } from '@dv/shared/data-access/fall';
import { FallHeaderStore } from '@dv/shared/data-access/fall-header';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import { SharedDialogNutzungsbedingungenComponent } from '@dv/shared/dialog/nutzungsbedingungen';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { PermissionStore } from '@dv/shared/global/permission';
import {
  SharedPatternGlobalHeaderComponent,
  SharedPatternGlobalHeaderPartsDirective,
} from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiInfoDialogComponent } from '@dv/shared/ui/info-dialog';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import {
  NavItem,
  NavMenuItem,
  createAllRouteParamsSig,
  createParamsIdSig,
  gesuchBaseMenuItems,
  gesuchBaseNavItems,
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
      <dv-shared-pattern-mobile-sidenav
        [staticNavItemsSig]="baseNavItems"
        [staticMenuItemsSig]="baseMenuItems"
        (closeSidenav)="sidenav.close()"
      >
      </dv-shared-pattern-mobile-sidenav>
    </mat-sidenav>
    <mat-sidenav-content class="tw:flex tw:flex-col">
      <dv-shared-pattern-global-header
        [staticNavItemsSig]="baseNavItems"
        [staticMenuItemsSig]="baseMenuItems"
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
  private store = inject(Store);
  private fallStore = inject(FallStore);
  private dialog = inject(MatDialog);
  private navigationStore = inject(NavigationStore);
  private router = inject(Router);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);
  private fallHeaderStore = inject(FallHeaderStore);
  private sozialdienstStore = inject(SozialdienstStore);
  private globalNotificationStore = inject(GlobalNotificationStore);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  baseNavItems = gesuchBaseNavItems;
  baseMenuItems = gesuchBaseMenuItems;

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  private allRouteParamsSig = createAllRouteParamsSig(this.router);

  private gesuchIdSig = createParamsIdSig('gesuchId', this.allRouteParamsSig);

  private availableSozialdiensteSig = computed(() => {
    const sozialdienste = this.sozialdienstStore.availableSozialdienste()?.data;

    return sozialdienste?.filter((sozialdienst) => sozialdienst.aktiv);
  });

  aktiveDelegierungSig = computed(() => {
    const delegierung =
      this.fallHeaderStore.fallHeaderViewSig()?.currentDelegierung;
    if (!delegierung || delegierung.status !== 'AKZEPTIERT') {
      return null;
    }

    return delegierung;
  });

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
      const fallId = this.fallStore.currentFallViewSig()?.id;
      const rolesMap = this.permissionStore.rolesMapSig();
      const fallHeader = this.fallHeaderStore.fallHeaderViewSig();
      const availableSozialdienste = this.availableSozialdiensteSig() ?? [];
      const delegierung = fallHeader?.currentDelegierung;

      if (!fallId) {
        this.navigationStore.setNavigationItems(gesuchBaseMenuItems);
        return;
      }

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

      const sozialdienstDelegieren: NavItem | undefined =
        availableSozialdienste.length && fallHeader && !delegierung
          ? {
              type: 'action',
              id: 'sozialdienst-delegieren',
              label: { key: 'shared.menu.delegieren' },
              action: () => this.delegiereSozialdienst(fallId),
            }
          : undefined;

      const navItems: NavItem[] = [
        ...gesuchBaseNavItems,
        fallDokumente,
        auszahlung,
        nachrichten,
        ...(sozialdienstDelegieren ? [sozialdienstDelegieren] : []),
      ].filter((item) => {
        if (!item.rolesAllowed || item.rolesAllowed.length === 0) {
          return true;
        }

        return item.rolesAllowed.some((role) => rolesMap[role]);
      });

      this.navigationStore.setNavigationItems(navItems);
    });

    // menuItems items effect
    effect(() => {
      const fallId = this.fallStore.currentFallViewSig()?.id;
      const rolesMap = this.permissionStore.rolesMapSig();

      if (!fallId) {
        return;
      }

      const allgemeineInformationen: NavMenuItem = {
        type: 'action',
        id: 'allgemeine-informationen',
        label: { key: 'shared.menu.allgemeine-informationen' },
        action: () => {
          SharedUiInfoDialogComponent.open(this.dialog, {
            data: {
              type: 'translated',
              titleKey: 'shared.allgemeine-informationen.title',
              messageKey: 'shared.allgemeine-informationen.message',
            },
          });
        },
      };

      const nutzungsbedingungen: NavMenuItem = {
        type: 'action',
        id: 'nutzungsbedingungen',
        label: { key: 'shared.menu.nutzungsbedingungen' },
        action: () => {
          const benutzer = this.benutzerSig();
          const nutzungsbedingungenAkzeptiert =
            benutzer?.nutzungsbedingungenAkzeptiert;
          const benutzerId = benutzer?.id;

          if (!benutzerId) return;

          SharedDialogNutzungsbedingungenComponent.open(
            this.dialog,
            nutzungsbedingungenAkzeptiert ?? false,
          )
            .afterClosed()
            .subscribe((result) => {
              if (result && benutzerId) {
                this.store.dispatch(
                  SharedDataAccessBenutzerApiEvents.nutzungsbedingungenAkzeptieren(
                    {
                      benutzerId,
                    },
                  ),
                );
              }
            });
        },
      };

      const menuItems: NavMenuItem[] = [
        ...gesuchBaseMenuItems,
        allgemeineInformationen,
        nutzungsbedingungen,
      ].filter((item) => {
        if (!item.rolesAllowed || item.rolesAllowed.length === 0) {
          return true;
        }

        return item.rolesAllowed.some((role) => rolesMap[role]);
      });

      this.navigationStore.setMenuItems(menuItems);
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
