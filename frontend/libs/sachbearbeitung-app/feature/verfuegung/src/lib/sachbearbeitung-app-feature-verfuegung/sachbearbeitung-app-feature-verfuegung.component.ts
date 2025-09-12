import { ChangeDetectionStrategy, Component } from '@angular/core';

import { VerfuegungOption } from '@dv/sachbearbeitung-app/model/verfuegung';
import { SachbearbeitungAppPatternVerfuegungLayoutComponent } from '@dv/sachbearbeitung-app/pattern/verfuegung-layout';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-verfuegung',
  imports: [
    SachbearbeitungAppPatternVerfuegungLayoutComponent,
    SharedUiRouterOutletWrapperComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-verfuegung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureVerfuegungComponent {
  option?: VerfuegungOption;
}
