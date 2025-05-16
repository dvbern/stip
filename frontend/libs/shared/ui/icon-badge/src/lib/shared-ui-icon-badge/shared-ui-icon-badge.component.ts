import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';

import { SharedModelState } from '@dv/shared/model/state-colors';
import { SharedUiBadgeComponent } from '@dv/shared/ui/badge';

@Component({
    selector: 'dv-shared-ui-icon-badge',
    imports: [CommonModule, SharedUiBadgeComponent],
    templateUrl: './shared-ui-icon-badge.component.html',
    styleUrl: './shared-ui-icon-badge.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SharedUiIconBadgeComponent {
  @Input() @HostBinding('class') type: SharedModelState = 'warning';
}
