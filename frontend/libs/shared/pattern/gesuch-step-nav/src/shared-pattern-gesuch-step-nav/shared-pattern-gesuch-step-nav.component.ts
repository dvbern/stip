import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  computed,
  inject,
  input,
} from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { GesuchFormStepView, StepState } from '@dv/shared/model/gesuch-form';
import { SharedUiChangeIndicatorComponent } from '@dv/shared/ui/change-indicator';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { stepHasChanges } from '@dv/shared/util-fn/gesuch-util';

import { sharedPatternGesuchStepNavView } from './shared-pattern-gesuch-step-nav.selectors';

@Component({
  selector: 'dv-shared-pattern-gesuch-step-nav',
  standalone: true,
  imports: [
    RouterLink,
    TranslatePipe,
    SharedUiIconChipComponent,
    SharedUiChangeIndicatorComponent,
    RouterLinkActive,
  ],
  templateUrl: './shared-pattern-gesuch-step-nav.component.html',
  styleUrls: ['./shared-pattern-gesuch-step-nav.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternGesuchStepNavComponent {
  @Output() navClicked = new EventEmitter();
  private store = inject(Store);

  statusIconMap: Record<StepState, string> = {
    VALID: 'check_circle',
    INVALID: 'error',
    WARNING: 'error',
  };
  stepsSig = input<GesuchFormStepView[]>();
  stepsViewSig = computed(() => {
    const { cachedGesuchId, trancheSetting, tranchenChanges } = this.viewSig();
    return this.stepsSig()?.map((step) => ({
      ...step,
      hasChanges: stepHasChanges(tranchenChanges, step),
      route: trancheSetting
        ? [
            '/',
            'gesuch',
            ...step.route.split('/'),
            cachedGesuchId,
            ...trancheSetting.routesSuffix,
          ]
        : null,
      isActive: this.route.isActive(`gesuch/${step.route}`, {
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
