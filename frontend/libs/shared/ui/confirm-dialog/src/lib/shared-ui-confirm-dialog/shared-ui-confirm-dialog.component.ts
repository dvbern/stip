import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

export interface ConfirmDialogData<T extends string> {
  title?: T;
  message?: T;
  confirmText?: string;
  cancelText?: string;
  translationObject?: unknown;
}

@Component({
  selector: 'dv-shared-ui-confirm-dialog',
  imports: [TranslocoPipe],
  templateUrl: './shared-ui-confirm-dialog.component.html',
  styleUrl: './shared-ui-confirm-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiConfirmDialogComponent<T extends string = string> {
  dialogRef = inject(MatDialogRef);
  dialogData = inject<ConfirmDialogData<T>>(MAT_DIALOG_DATA, {
    optional: true,
  });

  static open<T extends string = string>(
    dialog: MatDialog,
    data: ConfirmDialogData<T>,
  ) {
    return dialog.open<
      SharedUiConfirmDialogComponent,
      ConfirmDialogData<T>,
      boolean
    >(SharedUiConfirmDialogComponent, { data, maxWidth: '724px' });
  }

  confirm() {
    return this.dialogRef.close(true);
  }

  cancel() {
    return this.dialogRef.close(false);
  }
}
