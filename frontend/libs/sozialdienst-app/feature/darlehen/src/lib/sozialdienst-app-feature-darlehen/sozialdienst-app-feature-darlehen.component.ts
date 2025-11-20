import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiDarlehenComponent } from '@dv/shared/ui/darlehen';

@Component({
  selector: 'dv-sozialdienst-app-feature-darlehen',
  imports: [
    RouterLink,
    TranslocoDirective,
    SharedPatternMainLayoutComponent,
    SharedUiDarlehenComponent,
  ],
  templateUrl: './sozialdienst-app-feature-darlehen.component.html',
  styleUrl: './sozialdienst-app-feature-darlehen.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstAppFeatureDarlehenComponent {}
