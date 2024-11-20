import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslatePipe } from '@ngx-translate/core';

export interface ConfirmDialogData {
  title?: string;
  message?: string;
  confirmText?: string;
  cancelText?: string;
  translationObject?: unknown;
}

@Component({
  selector: 'dv-shared-ui-confirm-dialog',
  standalone: true,
  imports: [CommonModule, TranslatePipe],
  templateUrl: './shared-ui-confirm-dialog.component.html',
  styleUrl: './shared-ui-confirm-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiConfirmDialogComponent {
  dialogRef = inject(MatDialogRef);
  dialogData = inject<ConfirmDialogData>(MAT_DIALOG_DATA, { optional: true });

  static open(dialog: MatDialog, data: ConfirmDialogData) {
    return dialog.open<
      SharedUiConfirmDialogComponent,
      ConfirmDialogData,
      boolean
    >(SharedUiConfirmDialogComponent, { data });
  }

  confirm() {
    return this.dialogRef.close(true);
  }

  cancel() {
    return this.dialogRef.close(false);
  }
}
