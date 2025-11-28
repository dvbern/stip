import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { SharedPatternBasicLayoutComponent } from '@dv/shared/pattern/basic-layout';

@Component({
  selector: 'dv-demo-data-app-feature-demo-data-overview',
  imports: [SharedPatternBasicLayoutComponent, TranslocoDirective],
  templateUrl: './demo-data-app-feature-demo-data-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DemoDataAppFeatureDemoDataOverviewComponent {

}
