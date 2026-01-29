import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedPatternBasicLayoutComponent } from '@dv/shared/pattern/basic-layout';

@Component({
  selector: 'dv-shared-feature-unauthorized',
  imports: [TranslocoPipe, SharedPatternBasicLayoutComponent],
  templateUrl: './shared-feature-unauthorized.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureUnauthorizedComponent {}
