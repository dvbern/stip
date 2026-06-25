import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { Notification } from '@dv/shared/model/gesuch';
import { SharedUiTooltipDateComponent } from '@dv/shared/ui/tooltip-date';

@Component({
  selector: 'dv-shared-ui-notifications',
  imports: [
    TranslocoPipe,
    SharedUiTooltipDateComponent,
    RouterModule,
    CommonModule,
  ],
  templateUrl: './shared-ui-notifications.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiNotificationsComponent {
  notificationsSig = input.required<Notification[]>({
    // eslint-disable-next-line @angular-eslint/no-input-rename
    alias: 'notifications',
  });
  selectedNotificationIdSig = input<string | undefined>(undefined, {
    // eslint-disable-next-line @angular-eslint/no-input-rename
    alias: 'selectedNotificationId',
  });
}
