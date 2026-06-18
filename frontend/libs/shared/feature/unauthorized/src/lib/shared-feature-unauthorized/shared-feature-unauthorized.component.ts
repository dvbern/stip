import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

@Component({
  selector: 'dv-shared-feature-unauthorized',
  imports: [TranslocoPipe],
  templateUrl: './shared-feature-unauthorized.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureUnauthorizedComponent {}
