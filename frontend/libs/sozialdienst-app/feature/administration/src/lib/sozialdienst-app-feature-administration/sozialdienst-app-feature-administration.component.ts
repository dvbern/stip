import { ChangeDetectionStrategy, Component } from '@angular/core';

import { AdminOption } from '@dv/shared/model/router';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';
import { SozialdienstAppPatternAdministrationLayoutComponent } from '@dv/sozialdienst-app/pattern/administration-layout';

@Component({
  selector: 'dv-sozialdienst-app-feature-administration',
  imports: [
    SharedUiRouterOutletWrapperComponent,
    SozialdienstAppPatternAdministrationLayoutComponent,
  ],
  templateUrl: './sozialdienst-app-feature-administration.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationComponent {
  option?: AdminOption;
}
