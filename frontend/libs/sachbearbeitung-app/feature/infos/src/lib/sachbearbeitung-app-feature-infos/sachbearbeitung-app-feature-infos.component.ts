import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

import { InfosOptions } from '@dv/sachbearbeitung-app/model/infos';
import { SachbearbeitungAppPatterninfosLayoutComponent } from '@dv/sachbearbeitung-app/pattern/infos-layout';
import { SharedUiRouterOutletWrapperComponent } from '@dv/shared/ui/router-outlet-wrapper';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos',
  standalone: true,
  imports: [
    CommonModule,
    SharedUiRouterOutletWrapperComponent,
    SachbearbeitungAppPatterninfosLayoutComponent,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosComponent {
  option?: InfosOptions;
}
