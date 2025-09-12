import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

import { SachbearbeitungAppPatternBasicLayoutComponent } from '@dv/sachbearbeitung-app/pattern/basic-layout';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-unauthorized',
  imports: [TranslocoPipe, SachbearbeitungAppPatternBasicLayoutComponent],
  templateUrl: './sachbearbeitung-app-feature-unauthorized.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureUnauthorizedComponent {}
