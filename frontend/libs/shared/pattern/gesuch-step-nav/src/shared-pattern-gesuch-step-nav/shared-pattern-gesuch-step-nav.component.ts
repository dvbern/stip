import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostBinding,
  Output,
  computed,
  inject,
  input,
} from '@angular/core';
import { Router, RouterLink, isActive } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { isHistorizedView } from '@dv/shared/data-access/gesuch';
import {
  GesuchFormStepView,
  PERSON,
  StepState,
} from '@dv/shared/model/gesuch-form';
import { SharedUiChangeIndicatorComponent } from '@dv/shared/ui/change-indicator';
import { stepHasChanges } from '@dv/shared/util-fn/gesuch-util';

import { sharedPatternGesuchStepNavView } from './shared-pattern-gesuch-step-nav.selectors';

@Component({
  selector: 'dv-shared-pattern-gesuch-step-nav',
  imports: [RouterLink, TranslocoPipe, SharedUiChangeIndicatorComponent],
  templateUrl: './shared-pattern-gesuch-step-nav.component.html',
  styleUrls: ['./shared-pattern-gesuch-step-nav.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternGesuchStepNavComponent {
  @HostBinding('class') klass = 'tw:dv-pass-height tw:h-full tw:p-6';
  @Output() navClicked = new EventEmitter();
  private store = inject(Store);

  statusIconMap: Record<StepState, string> = {
    VALID: 'check_circle',
    INVALID: 'error',
    WARNING: 'error',
  };
  isHistorizedSig = this.store.selectSignal(isHistorizedView);
  stepsSig = input<GesuchFormStepView[]>();
  stepsViewSig = computed(() => {
    const { cachedGesuchId, trancheSetting, tranchenChanges } = this.viewSig();
    return this.stepsSig()?.map((step) => ({
      ...step,
      hasChanges: stepHasChanges(tranchenChanges, step),
      name: step.route,
      prependLine: step.route === PERSON.route,
      route: trancheSetting
        ? [
            '/',
            'gesuch',
            ...step.route.split('/'),
            cachedGesuchId,
            ...trancheSetting.routesSuffix,
          ]
        : null,
      active: isActive(`gesuch/${step.route}`, this.route, {
        paths: 'subset',
        queryParams: 'ignored',
        fragment: 'ignored',
        matrixParams: 'ignored',
      }),
    }));
  });
  viewSig = this.store.selectSignal(sharedPatternGesuchStepNavView);

  route = inject(Router);

  trackByIndex(index: number): number {
    return index;
  }
}
