import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';

import { SharedModelState } from '@dv/shared/model/state-colors';

@Component({
  selector: 'dv-shared-ui-icon-badge',
  imports: [],
  templateUrl: './shared-ui-icon-badge.component.html',
  styleUrl: './shared-ui-icon-badge.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiIconBadgeComponent {
  @Input() @HostBinding('class') type: SharedModelState = 'warning';
}
