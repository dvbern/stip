import { ChangeDetectionStrategy, Component } from '@angular/core';

import { SachbearbeitungAppPatternGesuchStepLayoutComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-step-layout';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedUiGesuchStepWrapperComponent } from '@dv/shared/ui/gesuch-step-wrapper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuch-form',
  standalone: true,
  imports: [
    SharedUiGesuchStepWrapperComponent,
    SachbearbeitungAppPatternGesuchStepLayoutComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-gesuch-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureGesuchFormComponent {
  step?: SharedModelGesuchFormStep;
}
