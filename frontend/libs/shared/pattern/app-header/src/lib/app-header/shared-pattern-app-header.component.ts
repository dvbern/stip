import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  Output,
  ViewChild,
  computed,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { OAuthService } from 'angular-oauth2-oidc';

import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import {
  SharedDataAccessLanguageEvents,
  selectLanguage,
} from '@dv/shared/data-access/language';
import { Language } from '@dv/shared/model/language';
import { capitalized } from '@dv/shared/model/type-util';
import { SharedUiLanguageSelectorComponent } from '@dv/shared/ui/language-selector';
import { SharedUiMandantStylesDvComponent } from '@dv/shared/ui/mandant-styles-dv';
import { SharedUtilTenantConfigService } from '@dv/shared/util/tenant-config';

@Component({
  selector: 'dv-shared-pattern-app-header',
  imports: [
    CommonModule,
    TranslocoPipe,
    RouterLink,
    MatMenuModule,
    MatButtonModule,
    SharedUiLanguageSelectorComponent,
    SharedUiMandantStylesDvComponent,
  ],
  templateUrl: './shared-pattern-app-header.component.html',
  styleUrls: ['./shared-pattern-app-header.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternAppHeaderComponent {
  @Input() backLink?: { path: string; text: string };
  @Input() isScroll = false;
  @Input() breakpointCompactHeader = '(max-width: 992px)';
  @Input() compactHeader = false;
  @Output() openSidenav = new EventEmitter<void>();
  @Output() closeSidenav = new EventEmitter<void>();
  @ViewChild('menu') menu!: ElementRef;

  protected readonly Breakpoints = Breakpoints;
  protected breakpointObserver = inject(BreakpointObserver);
  private oauthService = inject(OAuthService);
  private store = inject(Store);
  private cd = inject(ChangeDetectorRef);
  private tenantCacheService = inject(SharedUtilTenantConfigService);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  languageSig = this.store.selectSignal(selectLanguage);
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
  });
  tenantSig = this.tenantCacheService.tenantInfoSig;
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
