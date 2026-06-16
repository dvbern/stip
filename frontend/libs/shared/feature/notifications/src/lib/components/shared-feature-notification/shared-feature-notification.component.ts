/* eslint-disable @angular-eslint/no-input-rename */

import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
} from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

import { NotificationStore } from '@dv/shared/data-access/notification';
import { SharedUiTooltipDateComponent } from '@dv/shared/ui/tooltip-date';

@Component({
  selector: 'dv-shared-feature-notification',
  imports: [TranslocoPipe, SharedUiTooltipDateComponent],
  templateUrl: './shared-feature-notification.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureNotificationComponent {
  private notificationStore = inject(NotificationStore);

  notificationId = input<string | undefined>(undefined, {
    alias: 'notificationId',
  });

  notificationSig = computed(() => {
    const notificationId = this.notificationId();
    const notifications = this.notificationStore.notificationListViewSig();
    return notifications.find((n) => n.id === notificationId) ?? null;
  });
}
