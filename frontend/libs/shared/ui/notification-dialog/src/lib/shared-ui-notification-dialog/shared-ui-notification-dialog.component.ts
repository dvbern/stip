import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslatePipe } from '@ngx-translate/core';

import { Notification } from '@dv/shared/model/gesuch';
import { SharedUiTooltipDateComponent } from '@dv/shared/ui/tooltip-date';

type NotificationDialogData = {
  notification: Notification;
};

@Component({
  selector: 'dv-shared-ui-notification-dialog',
  standalone: true,
  imports: [CommonModule, TranslatePipe, SharedUiTooltipDateComponent],
  templateUrl: './shared-ui-notification-dialog.component.html',
  styleUrl: './shared-ui-notification-dialog.component.scss',
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
