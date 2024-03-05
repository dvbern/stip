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
  private store = inject(Store);

  stepsSig = input<
    (SharedModelGesuchFormStep & {
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
  @Output() navClicked = new EventEmitter();

  route = inject(Router);

  viewSig = this.store.selectSignal(sharedPatternGesuchStepNavView);

  trackByIndex(index: number): number {
    return index;
  }
}
