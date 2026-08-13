import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
} from '@angular/core';

import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';

@Component({
  selector: 'dv-shared-ui-dashboard-gesuch-subitem',
  imports: [CommonModule, SharedUiAdvTranslocoDirective],
  templateUrl: './shared-ui-dashboard-gesuch-subitem.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiDashboardGesuchSubItemComponent {
  statusSig = input.required<string>();
  @HostBinding('class') defaultClasses =
    'tw:flex tw:flex-col tw:rounded-lg tw:p-6 tw:pb-6';
}
