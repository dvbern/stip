import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedModelNachricht } from '@dv/shared/model/nachricht';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiTooltipDateComponent } from '@dv/shared/ui/tooltip-date';

type NotificationDialogData = {
  notification: SharedModelNachricht;
};

@Component({
  selector: 'dv-shared-ui-notification-dialog',
  imports: [
    TranslocoPipe,
    SharedUiTooltipDateComponent,
    SharedUiDownloadButtonDirective,
  ],
  templateUrl: './shared-ui-notification-dialog.component.html',
  styleUrl: './shared-ui-notification-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiNotificationDialogComponent {
  private dialogRef = inject(MatDialogRef);

  data = inject<NotificationDialogData>(MAT_DIALOG_DATA);

  static open(dialog: MatDialog, notification: SharedModelNachricht) {
    dialog.open(SharedUiNotificationDialogComponent, {
      data: { notification },
    });
  }

  close() {
    this.dialogRef.close();
  }
}
