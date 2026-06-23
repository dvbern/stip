/* eslint-disable @angular-eslint/no-input-rename */

import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

import { NotificationStore } from '@dv/shared/data-access/notification';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiTooltipDateComponent } from '@dv/shared/ui/tooltip-date';

@Component({
  selector: 'dv-shared-feature-notification',
  imports: [
    TranslocoPipe,
    SharedUiTooltipDateComponent,
    SharedUiDownloadButtonDirective,
  ],
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

  markAsRead(notificationId: string) {
    this.notificationStore.markNotificationAsRead$({ notificationId });
  }

  constructor() {
    effect(() => {
      const notificationId = this.notificationId();
      if (notificationId) {
        this.notificationStore.setSelectedNotificationId(notificationId);
      }
    });

    // mark as read
    effect(() => {
      const notification = this.notificationSig();
      if (notification && !notification.read) {
        setTimeout(() => {
          this.markAsRead(notification.id);
        }, 3000);
      }
    });
  }
}
