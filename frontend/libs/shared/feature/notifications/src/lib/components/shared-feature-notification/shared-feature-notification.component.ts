/* eslint-disable @angular-eslint/no-input-rename */

import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { RouterModule } from '@angular/router';

import { FallHeaderStore } from '@dv/shared/data-access/fall-header';
import { NotificationStore } from '@dv/shared/data-access/notification';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiTooltipDateComponent } from '@dv/shared/ui/tooltip-date';

@Component({
  selector: 'dv-shared-feature-notification',
  imports: [
    SharedUiAdvTranslocoDirective,
    SharedUiTooltipDateComponent,
    SharedUiDownloadButtonDirective,
    RouterModule,
  ],
  templateUrl: './shared-feature-notification.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureNotificationComponent {
  private notificationStore = inject(NotificationStore);
  private fallHeaderStore = inject(FallHeaderStore);

  notificationIdSig = input<string | undefined>(undefined, {
    alias: 'notificationId',
  });

  fallIdSig = input<string | undefined>(undefined, {
    alias: 'fallId',
  });

  notificationSig = computed(() => {
    const notificationId = this.notificationIdSig();

    const notifications = this.notificationStore.notificationListViewSig();
    return notifications.find((n) => n.id === notificationId) ?? null;
  });

  constructor() {
    effect(() => {
      const notificationId = this.notificationIdSig();
      if (notificationId) {
        this.notificationStore.setSelectedNotificationId(notificationId);
      }
    });

    // mark as read
    effect(() => {
      const notification = this.notificationSig();
      const fallId = this.fallIdSig();
      if (notification && !notification.read && fallId) {
        this.notificationStore.markNotificationAsRead$({
          req: { notificationId: notification.id },
          onSuccess: () => {
            this.fallHeaderStore.loadFallHeader$({ fallId });
          },
        });
      }
    });
  }
}
