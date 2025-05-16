import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  computed,
  inject,
  viewChild,
} from '@angular/core';
import { MatDrawer, MatSidenavModule } from '@angular/material/sidenav';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';
import { OAuthService } from 'angular-oauth2-oidc';

import { selectSharedDataAccessBenutzer } from '@dv/shared/data-access/benutzer';
import {
  SharedDataAccessLanguageEvents,
  selectLanguage,
} from '@dv/shared/data-access/language';
import { Language } from '@dv/shared/model/language';
import { SharedUiLanguageSelectorComponent } from '@dv/shared/ui/language-selector';

@Component({
    selector: 'dv-shared-pattern-mobile-sidenav',
    imports: [
        CommonModule,
        MatSidenavModule,
        TranslatePipe,
        SharedUiLanguageSelectorComponent,
    ],
    templateUrl: './shared-pattern-mobile-sidenav.component.html',
    styleUrl: './shared-pattern-mobile-sidenav.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SharedPatternMobileSidenavComponent {
  private store = inject(Store);
  private benutzerSig = this.store.selectSignal(selectSharedDataAccessBenutzer);
  private oauthService = inject(OAuthService);

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
