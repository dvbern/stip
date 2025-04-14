import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

import { SachbearbeitungAppPatternAdministrationLayoutComponent } from '@dv/sachbearbeitung-app/pattern/administration-layout';
import { AdminOption } from '@dv/shared/model/router';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration',
  standalone: true,
  imports: [
    CommonModule,
    SharedUiRouterOutletWrapperComponent,
    SachbearbeitungAppPatternAdministrationLayoutComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-administration.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationComponent {
  option?: AdminOption;
}
