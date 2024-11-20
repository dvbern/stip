import {
  ChangeDetectionStrategy,
  Component,
  TemplateRef,
  ViewChild,
  inject,
} from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { debounceTime, distinctUntilChanged, filter, map } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { StatusColor } from '@dv/shared/model/gesuch';
import { NotificationType } from '@dv/shared/model/global-notification';

const PANEL_MAP: Record<NotificationType, `mat-${StatusColor}`> = {
  SEVERE: 'mat-warn',
  ERROR: 'mat-warn',
  ERROR_PERMANENT: 'mat-warn',
  INFO: 'mat-info',
  WARNING: 'mat-caution',
  SUCCESS: 'mat-success',
};

const ALWAYS_REFRESH_TYPES: (NotificationType | undefined)[] = [
  'INFO',
  'SUCCESS',
];

const NOTIFICATION_TIME = 5000;

@Component({
  selector: 'dv-global-notifications',
  standalone: true,
  imports: [MatSnackBarModule, TranslatePipe],
  templateUrl: './global-notifications.component.html',
  styleUrls: ['./global-notifications.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GlobalNotificationsComponent {
  store = inject(GlobalNotificationStore);

  private router = inject(Router);
  private snackbar = inject(MatSnackBar);

  @ViewChild('notificationTemplate')
  notificationTemplate!: TemplateRef<unknown>;

  constructor() {
    toObservable(this.store.mostImportantNotificationSig)
      .pipe(
        // Only update snackbar if the notification type changes or the list of notifications is empty
        // but always refresh notifications of type INFO and SUCCESS
        distinctUntilChanged(
          (prev, cur) =>
            !ALWAYS_REFRESH_TYPES.includes(cur?.type) &&
            prev?.type === cur?.type &&
            !!cur,
        ),
        map((notification) => {
          if (notification) {
            /**
             * In desktop a container based snackbar layout with top alignment is being used
             * @see `material.overrides.scss`
             */
            this.snackbar.openFromTemplate(this.notificationTemplate, {
              verticalPosition: 'bottom',
              horizontalPosition: 'right',
              panelClass: PANEL_MAP[notification.type],
            });
          } else {
            this.snackbar.dismiss();
          }
          return notification;
        }),
        filter((notification) => !!notification?.autohide),
        debounceTime(NOTIFICATION_TIME),
        takeUntilDestroyed(),
      )
      .subscribe(() => {
        this.store.clearUnimportantNotifications();
      });
  }

  removeNotification(id: number): void {
    this.store.removeNotification(id);
  }
}
