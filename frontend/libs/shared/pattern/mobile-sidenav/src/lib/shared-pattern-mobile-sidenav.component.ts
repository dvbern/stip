import { PortalModule } from '@angular/cdk/portal';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostBinding,
  Output,
  computed,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { MatDrawer, MatSidenavModule } from '@angular/material/sidenav';
import { TranslocoDirective } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { OAuthService } from 'angular-oauth2-oidc';

import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import {
  SharedDataAccessLanguageEvents,
  selectLanguage,
} from '@dv/shared/data-access/language';
import { NavigationStore } from '@dv/shared/data-access/navigation';
import { Language } from '@dv/shared/model/language';
import { SharedUiLanguageSelectorComponent } from '@dv/shared/ui/language-selector';
import { SharedUiNavItemsComponent } from '@dv/shared/ui/nav-items';
import { SharedUiNavMenuItemsComponent } from '@dv/shared/ui/nav-menu-items';
import { NavItem, NavMenuItem } from '@dv/shared/util/navigation';

@Component({
  selector: 'dv-shared-pattern-mobile-sidenav',
  imports: [
    MatSidenavModule,
    SharedUiLanguageSelectorComponent,
    PortalModule,
    TranslocoDirective,
    SharedUiNavItemsComponent,
    SharedUiNavMenuItemsComponent,
    MatMenuModule,
  ],
  templateUrl: './shared-pattern-mobile-sidenav.component.html',
  styleUrl: './shared-pattern-mobile-sidenav.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternMobileSidenavComponent {
  @HostBinding('class') hostClass = 'tw:relative tw:w-full';

  staticNavItemsSig = input<NavItem[]>([]);
  staticMenuItemsSig = input<NavMenuItem[]>([]);

  private store = inject(Store);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);
  private oauthService = inject(OAuthService);
  navigationStore = inject(NavigationStore);

  navItemsSig = computed(() => {
    const navigationItems = this.navigationStore.navigationViewSig();

    if (navigationItems.length) {
      return navigationItems;
    }

    return this.staticNavItemsSig() ?? [];
  });

  menuItemsSig = computed(() => {
    const menuItems = this.navigationStore.menuItemsViewSig();

    if (menuItems.length) {
      return menuItems;
    }

    return this.staticMenuItemsSig() ?? [];
  });

  @Output() closeSidenav = new EventEmitter<void>();

  sideNavSig = viewChild.required(MatDrawer);
  languageSig = this.store.selectSignal(selectLanguage);
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });

  logout() {
    this.oauthService.revokeTokenAndLogout();
  }

  handleLanguageChangeHeader(language: Language) {
    this.store.dispatch(
      SharedDataAccessLanguageEvents.headerMenuSelectorChange({ language }),
    );
  }
}
