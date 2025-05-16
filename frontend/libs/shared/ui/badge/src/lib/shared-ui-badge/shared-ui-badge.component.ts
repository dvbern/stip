import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';

import { SharedModelState } from '@dv/shared/model/state-colors';

@Component({
    selector: 'dv-shared-ui-badge',
    imports: [CommonModule],
    templateUrl: './shared-ui-badge.component.html',
    styleUrl: './shared-ui-badge.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SharedUiBadgeComponent {
  @Input() @HostBinding('class') type: SharedModelState = 'warning';
}
