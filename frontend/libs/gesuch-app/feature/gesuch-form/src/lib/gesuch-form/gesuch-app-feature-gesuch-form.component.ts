import { ChangeDetectionStrategy, Component, signal } from '@angular/core';

import { GesuchAppPatternGesuchStepLayoutComponent } from '@dv/gesuch-app/pattern/gesuch-step-layout';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';

@Component({
  selector: 'dv-gesuch-app-feature-gesuch-form',
  standalone: true,
  imports: [
    GesuchAppPatternGesuchStepLayoutComponent,
    SharedUiRouterOutletWrapperComponent,
  ],
  templateUrl: './gesuch-app-feature-gesuch-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureGesuchFormComponent {
  stepSig = signal<SharedModelGesuchFormStep | undefined>(undefined);
}
