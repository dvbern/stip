import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { SharedPatternMainLayoutComponent } from '@dv/shared/pattern/main-layout';
import { SharedUiDarlehenComponent } from '@dv/shared/ui/darlehen';

@Component({
  selector: 'dv-gesuch-app-feature-darlehen',
  imports: [
    RouterLink,
    TranslocoDirective,
    SharedPatternMainLayoutComponent,
    SharedUiDarlehenComponent,
  ],
  templateUrl: './gesuch-app-feature-darlehen.component.html',
  styleUrl: './gesuch-app-feature-darlehen.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureDarlehenComponent {}
