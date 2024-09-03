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
import { TranslateModule } from '@ngx-translate/core';

import {
  GesuchFormStepView,
  StepState,
  TRANCHE,
} from '@dv/shared/model/gesuch-form';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

import { sharedPatternGesuchStepNavView } from './shared-pattern-gesuch-step-nav.selectors';

@Component({
  selector: 'dv-shared-pattern-gesuch-step-nav',
  standalone: true,
  imports: [
    RouterLink,
    TranslateModule,
    SharedUiIconChipComponent,
    RouterLinkActive,
  ],
  templateUrl: './shared-pattern-gesuch-step-nav.component.html',
  styleUrls: ['./shared-pattern-gesuch-step-nav.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternGesuchStepNavComponent {
  @Output() navClicked = new EventEmitter();
  private store = inject(Store);

  trancheRouteSig = computed(() => {
    const gesuchsId = this.viewSig().cachedGesuchId;
    return ['/', 'gesuch', TRANCHE.route, gesuchsId];
  });
  statusIconMap: Record<StepState, string> = {
    VALID: 'check_circle',
    INVALID: 'error',
    WARNING: 'error',
  };
  stepsSig = input<GesuchFormStepView[]>();
  stepsViewSig = computed(() =>
    this.stepsSig()?.map((step) => ({
      ...step,
      routes: [
        '/',
        'gesuch',
        ...step.route.split('/'),
        this.viewSig().cachedGesuchId,
      ],
      isActive: this.route.isActive(`gesuch/${step.route}`, {
        paths: 'subset',
        queryParams: 'ignored',
        fragment: 'ignored',
        matrixParams: 'ignored',
      }),
    })),
  );
  viewSig = this.store.selectSignal(sharedPatternGesuchStepNavView);

  route = inject(Router);

  trackByIndex(index: number): number {
    return index;
  }
}
