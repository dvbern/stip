import {
  ChangeDetectionStrategy,
  Component,
  TemplateRef,
  ViewChild,
  inject,
} from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { TranslateModule } from '@ngx-translate/core';
import { debounceTime, distinctUntilChanged, filter, map } from 'rxjs';

import { GlobalNotificationStore } from '@dv/shared/data-access/global-notification';
import { NotificationType } from '@dv/shared/model/global-notification';

const PANEL_MAP: Record<NotificationType, string> = {
  SEVERE: 'error',
  ERROR: 'error',
  INFO: 'accent',
  WARNING: 'warn',
  SUCCESS: 'success',
};
const ALWAYS_REFRESH_TYPES: (NotificationType | undefined)[] = [
  'INFO',
  'SUCCESS',
];

@Component({
  selector: 'dv-global-notifications',
  standalone: true,
  imports: [MatSnackBarModule, TranslateModule],
  templateUrl: './global-notifications.component.html',
  styleUrls: ['./global-notifications.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GlobalNotificationsComponent {
  store = inject(GlobalNotificationStore);

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
        debounceTime(3000),
        takeUntilDestroyed(),
      )
      .subscribe(() => {
        this.store.clearUnimportantNotificaitons();
      });
  }

  removeNotification(id: number): void {
    this.store.removeNotification(id);
  }
}
