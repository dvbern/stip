/* eslint-disable @angular-eslint/no-input-rename */
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  input,
} from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';

import { NotificationStore } from '@dv/shared/data-access/notification';
import { SharedModelNachricht } from '@dv/shared/model/nachricht';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedUiNotificationsComponent } from '@dv/shared/ui/notifications';

@Component({
  selector: 'dv-shared-feature-notifications',
  imports: [RouterOutlet, SharedUiNotificationsComponent],
  templateUrl: './shared-feature-notifications.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureNotificationsComponent {
  notificationStore = inject(NotificationStore);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  // todo: needs fallback to cache? fallIdFromGesuchCacheSig
  fallIdParamSig = input<string | undefined>(undefined, { alias: 'fallId' });

  selectNotification(notification: SharedModelNachricht) {
    this.router.navigate([notification.id], { relativeTo: this.route });
  }

  constructor() {
    effect(() => {
      const fallId = this.fallIdParamSig();
      if (isDefined(fallId)) {
        this.notificationStore.getNotificationsForFall$({
          fallId: fallId,
        });
      }
    });
  }
}
