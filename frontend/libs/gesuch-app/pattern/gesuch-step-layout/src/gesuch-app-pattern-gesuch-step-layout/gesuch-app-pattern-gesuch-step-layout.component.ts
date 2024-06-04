import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  computed,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { filter } from 'rxjs';

import { GesuchAppPatternMainLayoutComponent } from '@dv/gesuch-app/pattern/main-layout';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchValidationView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import {
  SharedDataAccessLanguageEvents,
  selectLanguage,
} from '@dv/shared/data-access/language';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';
import { Language } from '@dv/shared/model/language';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiLanguageSelectorComponent } from '@dv/shared/ui/language-selector';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { getLatestGesuchIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { isDefined } from '@dv/shared/util-fn/type-guards';

@Component({
  selector: 'dv-gesuch-app-pattern-gesuch-step-layout',
  standalone: true,
  imports: [
    CommonModule,
    SharedPatternGesuchStepNavComponent,
    SharedUiProgressBarComponent,
    TranslateModule,
    SharedUiIconChipComponent,
    SharedUiLanguageSelectorComponent,
    GesuchAppPatternMainLayoutComponent,
    RouterLink,
    RouterLinkActive,
  ],
  templateUrl: './gesuch-app-pattern-gesuch-step-layout.component.html',
  styleUrls: ['./gesuch-app-pattern-gesuch-step-layout.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppPatternGesuchStepLayoutComponent {
  @Input()
  step?: SharedModelGesuchFormStep;

  navClicked = new EventEmitter();

  private store = inject(Store);

  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  validationViewSig = this.store.selectSignal(
    selectSharedDataAccessGesuchValidationView,
  );
  stepsSig = computed(() => {
    const { cachedGesuchFormular, invalidFormularProps } =
      this.validationViewSig();
    return this.stepManager.getAllStepsWithStatus(
      cachedGesuchFormular,
      invalidFormularProps.validations,
    );
  });

  constructor() {
    getLatestGesuchIdFromGesuchOnUpdate$(this.viewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchId) => {
        this.store.dispatch(
          SharedDataAccessGesuchEvents.gesuchValidateSteps({ id: gesuchId }),
        );
      });
  }

  handleLanguageChangeHeader(language: Language) {
    this.store.dispatch(
      SharedDataAccessLanguageEvents.headerMenuSelectorChange({ language }),
    );
  }
}
