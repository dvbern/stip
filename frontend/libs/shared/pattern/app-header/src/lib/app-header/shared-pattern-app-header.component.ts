import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  HostListener,
  Input,
  OnChanges,
  SimpleChanges,
  ViewChild,
  computed,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { OAuthService } from 'angular-oauth2-oidc';

import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import {
  SharedDataAccessLanguageEvents,
  selectLanguage,
} from '@dv/shared/data-access/language';
import { Language } from '@dv/shared/model/language';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLanguageSelectorComponent } from '@dv/shared/ui/language-selector';

@Component({
  selector: 'dv-shared-pattern-app-header',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    RouterOutlet,
    RouterLink,
    MatMenuModule,
    MatButtonModule,
    SharedUiLanguageSelectorComponent,
    SharedUiIconChipComponent,
  ],
  templateUrl: './shared-pattern-app-header.component.html',
  styleUrls: ['./shared-pattern-app-header.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternAppHeaderComponent implements OnChanges {
  @Input() backLink?: { path: string; text: string };
  @Input() closeMenu: { value?: unknown } | null = null;
  @Input() isScroll = false;
  @Input() breakpointCompactHeader = '(max-width: 992px)';
  @Input() compactHeader = false;
  @ViewChild('menu') menu!: ElementRef;

  protected readonly Breakpoints = Breakpoints;
  protected breakpointObserver = inject(BreakpointObserver);
  private oauthService = inject(OAuthService);
  // private offCanvasService = inject(NgbOffcanvas);
  private store = inject(Store);
  private cd = inject(ChangeDetectorRef);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);

  languageSig = this.store.selectSignal(selectLanguage);
  benutzerNameSig = computed(() => {
    const benutzer = this.benutzerSig();
    return `${benutzer?.vorname} ${benutzer?.nachname}`;
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

  @HostListener('window:scroll', ['$event']) handleScroll() {
    this.isScroll = window.scrollY > 0;
  }

  openMenu() {
    // this.offCanvasService.open(this.menu, { position: 'end', scroll: true });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['closeMenu']) {
      // this.offCanvasService.dismiss(changes['closeMenu'].currentValue);
    }
  }

  logout() {
    this.oauthService.revokeTokenAndLogout();
  }

  handleLanguageChangeHeader(language: Language) {
    this.store.dispatch(
      SharedDataAccessLanguageEvents.headerMenuSelectorChange({ language }),
    );
  }

  handleLanguageChangeFooter(language: Language) {
    this.store.dispatch(
      SharedDataAccessLanguageEvents.footerSelectorChange({ language }),
    );
  }
}
