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

import {
  SharedDataAccessBenutzerApiEvents,
  selectSharedDataAccessBenutzer,
} from '@dv/shared/data-access/benutzer';
import { FallStore } from '@dv/shared/data-access/fall';
import { FallHeaderStore } from '@dv/shared/data-access/fall-header';
import { selectSharedDataAccessGesuchCache } from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { SharedDialogNutzungsbedingungenComponent } from '@dv/shared/dialog/nutzungsbedingungen';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiInfoDialogComponent } from '@dv/shared/ui/info-dialog';
import {
  NavItem,
  NavMenuItem,
  createAllRouteParamsSig,
  createParamsIdSig,
  sozialdienstAdminNavItems,
  sozialdienstBaseMenuItems,
  sozialdienstBaseNavItems,
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
        (openSidenav)="sidenav.open()"
      ></dv-shared-pattern-global-header>

      <main class="tw:dv-page-body tw:flex tw:flex-col">
        <router-outlet></router-outlet>
      </main>
    </mat-sidenav-content>
  </mat-sidenav-container>`,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppPatternMainLayoutComponent {
  private fallStore = inject(FallStore);
  private navigationStore = inject(NavigationStore);
  private router = inject(Router);
  private dialog = inject(MatDialog);
  private gesuchHeaderStore = inject(GesuchHeaderStore);
  private permissionStore = inject(PermissionStore);
  private store = inject(Store);
  private fallHeaderStore = inject(FallHeaderStore);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  baseNavItems = sozialdienstBaseNavItems;
  baseMenuItems = sozialdienstBaseMenuItems;

  @HostBinding('class')
  hostClass = 'tw:flex tw:flex-col';

  cacheSig = this.store.selectSignal(selectSharedDataAccessGesuchCache);

  fallIdFromGesuchCacheSig = computed(() => {
    const { gesuch } = this.cacheSig();
    return gesuch?.fallId;
  });

  private allRouteParamsSig = createAllRouteParamsSig(this.router);

  private gesuchIdSig = createParamsIdSig('gesuchId', this.allRouteParamsSig);

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
      // Read allRouteParamsSig to re-run on every navigation
      this.allRouteParamsSig();
      const fallId = this.fallIdSig();
      if (fallId) {
        this.fallHeaderStore.loadFallHeader$({ fallId });
      }
    });

    // navigation items effect
    effect(() => {
      // Read allRouteParamsSig to re-run on every navigation
      this.allRouteParamsSig();
      const fallId = this.fallIdSig();
      const rolesMap = this.permissionStore.rolesMapSig();
      const fallHeader = this.fallHeaderStore.fallHeaderViewSig();

      if (!fallId) {
        // todo: also filter for roles here!
        this.navigationStore.setNavigationItems([
          ...sozialdienstBaseNavItems,
          ...sozialdienstAdminNavItems,
        ]);
        return;
      }

      const fallNav: NavItem = {
        type: 'link',
        id: 'fall',
        label: { key: 'shared.header.fall' },
        icon: 'assignment_ind',
        route: ['/fall', fallId],
      };

      const auszahlung: NavItem = {
        type: 'link',
        id: 'auszahlung',
        label: { key: 'shared.header.auszahlung' },
        icon: 'payments',
        route: ['/auszahlung', fallId],
      };

      const fallDokumente: NavItem = {
        type: 'link',
        id: 'fall-dokumente',
        label: { key: 'shared.menu.fallDokumente' },
        icon: 'description',
        route: ['/fall-dokumente', fallId],
      };

      const nachrichten: NavItem[] = [
        {
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
        },
        {
          type: 'separator',
          id: 'separator-1',
          orientation: 'vertical',
        },
      ];

      const navItems: NavItem[] = [
        fallNav,
        fallDokumente,
        auszahlung,
        ...nachrichten,
        ...sozialdienstBaseNavItems,
        ...sozialdienstAdminNavItems,
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
        ...sozialdienstBaseMenuItems,
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
}
