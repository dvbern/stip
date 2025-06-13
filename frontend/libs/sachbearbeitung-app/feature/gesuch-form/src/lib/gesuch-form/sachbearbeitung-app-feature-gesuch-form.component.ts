import { ChangeDetectionStrategy, Component } from '@angular/core';

import { SachbearbeitungAppPatternGesuchStepLayoutComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-step-layout';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuch-form',
  imports: [
    SharedUiRouterOutletWrapperComponent,
    SachbearbeitungAppPatternGesuchStepLayoutComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-gesuch-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureGesuchFormComponent {
  step?: GesuchFormStep;
}
