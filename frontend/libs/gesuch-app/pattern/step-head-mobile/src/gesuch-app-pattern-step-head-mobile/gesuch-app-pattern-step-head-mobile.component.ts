import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';

@Component({
  selector: 'dv-gesuch-app-pattern-step-head-mobile',
  standalone: true,
  imports: [SharedUiProgressBarComponent, TranslateModule],
  templateUrl: './gesuch-app-pattern-step-head-mobile.component.html',
  styleUrls: ['./gesuch-app-pattern-step-head-mobile.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppPatternStepHeadMobileComponent {}
