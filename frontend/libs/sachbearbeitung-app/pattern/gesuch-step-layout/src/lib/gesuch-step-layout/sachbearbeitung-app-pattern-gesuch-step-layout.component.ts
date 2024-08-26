import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  computed,
  inject,
  input,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { SachbearbeitungAppPatternGesuchHeaderComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-header';
import {
  selectSharedDataAccessGesuchCache,
  selectSharedDataAccessGesuchStepsView,
  selectSharedDataAccessGesuchsView,
} from '@dv/shared/data-access/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedPatternAppHeaderPartsDirective } from '@dv/shared/pattern/app-header';
import { SharedPatternGesuchStepNavComponent } from '@dv/shared/pattern/gesuch-step-nav';
import { GlobalNotificationsComponent } from '@dv/shared/pattern/global-notification';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { SharedUiSearchComponent } from '@dv/shared/ui/search';
import { SharedUtilGesuchFormStepManagerService } from '@dv/shared/util/gesuch-form-step-manager';

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
})
export class SachbearbeitungAppPatternGesuchStepLayoutComponent {
  stepSig = input<SharedModelGesuchFormStep | undefined>(undefined, {
    alias: 'step',
  });
  navClicked$ = new EventEmitter();

  stepManager = inject(SharedUtilGesuchFormStepManagerService);
  private store = inject(Store);
  cacheSig = this.store.selectSignal(selectSharedDataAccessGesuchCache);
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  stepsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchStepsView);
  stepsSig = computed(() =>
    this.stepManager.getValidatedSteps(
      this.stepsViewSig().steps,
      this.cacheSig().gesuchFormular,
      undefined,
      this.viewSig().readonly,
    ),
  );
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
}
