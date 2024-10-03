import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  computed,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { combineLatest, filter } from 'rxjs';

import { SachbearbeitungAppPatternGesuchHeaderComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-header';
import {
  SharedDataAccessGesuchEvents,
  selectSharedDataAccessGesuchCacheView,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchValidationView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedPatternAppHeaderPartsDirective } from '@dv/shared/pattern/app-header';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { SharedUiSearchComponent } from '@dv/shared/ui/search';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';
import { SharedUtilHeaderService } from '@dv/shared/util/header';
import { isDefined } from '@dv/shared/util-fn/type-guards';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-gesuch-step-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule,
    GlobalNotificationsComponent,
    SharedPatternGesuchStepNavComponent,
    SharedPatternAppHeaderPartsDirective,
    SharedUiIconChipComponent,
    SharedUiProgressBarComponent,
    SharedUiSearchComponent,
    SachbearbeitungAppPatternGesuchHeaderComponent,
  ],
  templateUrl:
    './sachbearbeitung-app-pattern-gesuch-step-layout.component.html',
  styleUrls: [
    './sachbearbeitung-app-pattern-gesuch-step-layout.component.scss',
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [SharedUtilHeaderService],
})
export class SachbearbeitungAppPatternGesuchStepLayoutComponent {
  stepSig = input<SharedModelGesuchFormStep | undefined>(undefined, {
    alias: 'step',
  });
  navClicked$ = new EventEmitter();

  private store = inject(Store);
  headerService = inject(SharedUtilHeaderService);
  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  cacheViewSig = this.store.selectSignal(selectSharedDataAccessGesuchCacheView);
  stepsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);
  validationViewSig = this.store.selectSignal(
    selectSharedDataAccessGesuchValidationView,
  );
  stepsSig = computed(() => {
    const { cachedGesuchFormular, invalidFormularProps } =
      this.validationViewSig();
    const steps = this.stepsViewSig().steps;
    const readonly = this.cacheViewSig().readonly;
    const validatedSteps = this.stepManager.getValidatedSteps(
      steps,
      cachedGesuchFormular,
      invalidFormularProps.validations,
      readonly,
    );
    return validatedSteps;
  });
  currentStepProgressSig = computed(() => {
    const currentStep = this.stepSig();
    const stepsFlow = this.stepsViewSig().stepsFlow;
    return this.stepManager.getStepProgress(stepsFlow, currentStep);
  });
  currentStepSig = computed(() => {
    const currentStep = this.stepSig();
    const steps = this.stepsSig();
    return steps.find((step) => step.route === currentStep?.route);
  });

  constructor() {
    combineLatest([
      getLatestTrancheIdFromGesuchOnUpdate$(this.viewSig).pipe(
        filter(isDefined),
      ),
    ])
      .pipe(takeUntilDestroyed())
      .subscribe(([gesuchTrancheId]) => {
        this.store.dispatch(
          SharedDataAccessGesuchEvents.gesuchValidateSteps({ gesuchTrancheId }),
        );
      });
  }
}
