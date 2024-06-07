import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';

import { SharedModelState } from '@dv/shared/model/state-colors';

@Component({
  selector: 'dv-shared-ui-severity-icon',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shared-ui-severity-icon.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiSeverityIconComponent {
  type = input.required<SharedModelState>();
}
