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

import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';
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

  stepsSig = input<
    (SharedModelGesuchFormStep & {
      valid?: boolean;
      disabled: boolean;
    })[]
  >();
  stepsViewSig = computed(
    () =>
      this.stepsSig()?.map((step) => ({
        ...step,
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
