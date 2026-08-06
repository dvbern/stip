/* eslint-disable @angular-eslint/no-input-rename */
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnDestroy,
  effect,
  inject,
  input,
  untracked,
} from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { NotificationStore } from '@dv/shared/data-access/notification';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiNotificationsComponent } from '@dv/shared/ui/notifications';

@Component({
  selector: 'dv-shared-feature-notifications',
  imports: [
    RouterOutlet,
    SharedUiNotificationsComponent,
    SharedUiIconChipComponent,
    TranslocoPipe,
  ],
  templateUrl: './shared-feature-notifications.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureNotificationsComponent implements OnDestroy {
  @HostBinding('class') class = 'tw:dv-pass-height tw:dv-container';

  notificationStore = inject(NotificationStore);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  fallIdParamSig = input<string | undefined>(undefined, { alias: 'fallId' });

  constructor() {
    effect(() => {
      const fallId = this.fallIdParamSig();
      if (isDefined(fallId)) {
        this.notificationStore.getNotificationsForFall$({
          req: { fallId },
          onSuccess: (notifications) => {
            const selectedNotificationId = untracked(
              this.notificationStore.selectedNotificationId,
            );

            if (!selectedNotificationId && notifications[0]?.id) {
              this.router.navigate([notifications[0].id], {
                relativeTo: this.route,
                replaceUrl: true,
              });
            }
          },
        });
      }
    });
  }

  ngOnDestroy() {
    this.notificationStore.setSelectedNotificationId(undefined);
  }
}
