/* eslint-disable @angular-eslint/no-input-rename */
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  effect,
  inject,
  input,
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
export class SharedFeatureNotificationsComponent {
  @HostBinding('class') class = 'tw:dv-pass-height tw:p-6';

  notificationStore = inject(NotificationStore);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  // todo: needs fallback to cache for soz? fallIdFromGesuchCacheSig
  fallIdParamSig = input<string | undefined>(undefined, { alias: 'fallId' });

  // selectNotification(notification: SharedModelNachricht) {
  //   this.router.navigate([notification.id], { relativeTo: this.route });
  // }

  constructor() {
    effect(() => {
      const fallId = this.fallIdParamSig();
      if (isDefined(fallId)) {
        this.notificationStore.getNotificationsForFall$({
          req: { fallId },
          onSuccess: (notifications) => {
            this.router.navigate([notifications[0]?.id], {
              relativeTo: this.route,
            });
          },
        });
      }
    });
  }
}
