import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

import { Notification } from '@dv/shared/model/gesuch';

type NotificationDialogData = {
  notification: Notification;
};

@Component({
  selector: 'dv-shared-ui-notification-dialog',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './shared-ui-notification-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiNotificationDialogComponent {
  private dialogRef = inject(MatDialogRef);

  data = inject<NotificationDialogData>(MAT_DIALOG_DATA);

  static open(dialog: MatDialog, notification: Notification) {
    dialog.open(SharedUiNotificationDialogComponent, {
      data: { notification },
    });
  }

  close() {
    this.dialogRef.close();
  }
}
