import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
} from '@angular/core';

import { SharedModelState } from '@dv/shared/model/state-colors';

const typeMap = {
  danger: {
    baseStyle: 'tw:border-dv-main-danger tw:bg-dv-red-subtle',
    color: 'tw:text-dv-red',
    icon: 'warning',
  },
  info: {
    baseStyle: 'tw:border-dv-blue tw:bg-dv-blue-subtle',
    color: 'tw:text-dv-blue',
    icon: 'schedule',
  },
  success: {
    baseStyle: 'tw:border-dv-green tw:bg-dv-green-subtle',
    color: 'tw:text-dv-green',
    icon: 'check_circle',
  },
  warning: {
    baseStyle: 'tw:border-dv-yellow tw:bg-dv-yellow-subtle',
    color: 'tw:text-dv-yellow',
    icon: 'error',
  },
} as const satisfies Record<SharedModelState, unknown>;

@Component({
  selector: 'dv-shared-ui-icon-badge',
  imports: [CommonModule],
  templateUrl: './shared-ui-icon-badge.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiIconBadgeComponent {
  typeSig = input.required({
    transform: (type: SharedModelState) => typeMap[type],
  });

  @HostBinding('class') klass = 'tw:block';
}
