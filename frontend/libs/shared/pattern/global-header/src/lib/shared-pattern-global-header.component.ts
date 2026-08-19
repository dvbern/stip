import { BreakpointObserver } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  HostBinding,
  HostListener,
  Input,
  Output,
  computed,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink } from '@angular/router';
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
import { capitalized } from '@dv/shared/model/type-util';
import { SharedUiLanguageSelectorComponent } from '@dv/shared/ui/language-selector';
import { SharedUiNavItemsComponent } from '@dv/shared/ui/nav-items';
import { SharedUiNavMenuItemsComponent } from '@dv/shared/ui/nav-menu-items';
import { SharedUiTenantStylesDvComponent } from '@dv/shared/ui/tenant-styles-dv';
import { NavItem, NavMenuItem } from '@dv/shared/util/navigation';
import { SharedUtilTenantConfigService } from '@dv/shared/util/tenant-config';

@Component({
  selector: 'dv-shared-pattern-global-header',
  imports: [
    CommonModule,
    RouterLink,
    MatMenuModule,
    MatButtonModule,
    SharedUiLanguageSelectorComponent,
    SharedUiTenantStylesDvComponent,
    TranslocoDirective,
    SharedUiNavItemsComponent,
    SharedUiNavMenuItemsComponent,
  ],
  templateUrl: './shared-pattern-global-header.component.html',
  styles: `
    header {
      background-color: var(--dv-body-bg);
      position: fixed;
      max-width: var(--content-max-width);
    }
  `,
})
export class SharedPatternGlobalHeaderComponent {
  @HostBinding('class') klass = 'tw:block';

  @Input() isScroll = false;
  @Input() breakpointCompactHeader = '(max-width: 992px)';
  @Input() compactHeader = false;

  staticNavItemsSig = input<NavItem[]>([]);
  staticMenuItemsSig = input<NavMenuItem[]>([]);

  @Output() openSidenav = new EventEmitter<void>();

  protected breakpointObserver = inject(BreakpointObserver);
  private oauthService = inject(OAuthService);
  private cd = inject(ChangeDetectorRef);
  private store = inject(Store);
  private tenantCacheService = inject(SharedUtilTenantConfigService);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);
  private dialog = inject(MatDialog);

  languageSig = this.store.selectSignal(selectLanguage);
  navigationStore = inject(NavigationStore);
  tenantSig = this.tenantCacheService.tenantInfoSig;

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

  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });
  logoSig = computed(() => {
    const identifier = this.tenantSig()?.identifier;
    if (!identifier) {
      return null;
    }
    return {
      src: `assets/images/logo_kanton_${identifier}_full.svg`,
      name: capitalized(identifier),
    };
  });

  constructor() {
    this.breakpointObserver
      .observe(this.breakpointCompactHeader)
      .pipe(takeUntilDestroyed())
      .subscribe((result) => {
        this.compactHeader = result.matches;
        this.cd.markForCheck();
      });
  }

  @HostListener('window:scroll') handleScroll() {
    this.isScroll = window.scrollY > 0;
  }

  logout() {
    this.oauthService.revokeTokenAndLogout();
  }

  handleLanguageChangeHeader(language: Language) {
    this.store.dispatch(
      SharedDataAccessLanguageEvents.headerMenuSelectorChange({ language }),
    );
  }
}
