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
import { SharedUiMandantStylesDvComponent } from '@dv/shared/ui/mandant-styles-dv';
import { SharedUiNavItemsComponent } from '@dv/shared/ui/nav-items';
import { NavItem } from '@dv/shared/util/navigation';
import { SharedUtilTenantConfigService } from '@dv/shared/util/tenant-config';

@Component({
  selector: 'dv-shared-pattern-global-header',
  imports: [
    CommonModule,
    RouterLink,
    MatMenuModule,
    MatButtonModule,
    SharedUiLanguageSelectorComponent,
    SharedUiMandantStylesDvComponent,
    TranslocoDirective,
    SharedUiNavItemsComponent,
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

  @Output() openSidenav = new EventEmitter<void>();
  @Output() closeSidenav = new EventEmitter<void>();

  protected breakpointObserver = inject(BreakpointObserver);
  private oauthService = inject(OAuthService);
  private cd = inject(ChangeDetectorRef);
  private store = inject(Store);
  private tenantCacheService = inject(SharedUtilTenantConfigService);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  languageSig = this.store.selectSignal(selectLanguage);
  navigationStore = inject(NavigationStore);
  tenantSig = this.tenantCacheService.tenantInfoSig;

  navItemsSig = computed(() => {
    const dynamicItems = this.navigationStore.navigationViewSig();

    if (dynamicItems.length) {
      return dynamicItems;
    }

    return this.staticNavItemsSig() ?? [];
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
