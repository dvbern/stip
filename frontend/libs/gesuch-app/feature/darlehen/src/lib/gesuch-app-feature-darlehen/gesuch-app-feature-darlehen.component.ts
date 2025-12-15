import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { SharedFeatureDarlehenComponent } from '@dv/shared/feature/darlehen';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';

@Component({
  selector: 'dv-gesuch-app-feature-darlehen',
  imports: [
    RouterLink,
    TranslocoDirective,
    SharedPatternMainLayoutComponent,
    SharedFeatureDarlehenComponent,
  ],
  templateUrl: './gesuch-app-feature-darlehen.component.html',
  styleUrl: './gesuch-app-feature-darlehen.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureDarlehenComponent {
  hasUnsavedChanges = false;
}
