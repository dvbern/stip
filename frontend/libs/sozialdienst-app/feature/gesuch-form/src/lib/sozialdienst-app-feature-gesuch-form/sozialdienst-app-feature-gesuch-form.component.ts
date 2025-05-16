import { ChangeDetectionStrategy, Component } from '@angular/core';

import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { GesuchAppPatternGesuchStepLayoutComponent } from '@dv/sozialdienst-app/pattern/gesuch-step-layout';

@Component({
  selector: 'dv-sozialdienst-app-feature-gesuch-form',
  imports: [
    SharedUiRouterOutletWrapperComponent,
    GesuchAppPatternGesuchStepLayoutComponent,
  ],
  templateUrl: './sozialdienst-app-feature-gesuch-form.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureGesuchFormComponent {
  step?: GesuchFormStep;
}
