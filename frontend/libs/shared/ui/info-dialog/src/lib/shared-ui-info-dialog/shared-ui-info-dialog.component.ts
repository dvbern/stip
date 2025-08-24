import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

export interface InfoDialogData {
  titleKey: string;
  messageKey: string;
}

@Component({
  selector: 'dv-shared-ui-info-dialog',
  imports: [CommonModule, TranslocoPipe],
  templateUrl: './shared-ui-info-dialog.component.html',
  styleUrl: './shared-ui-info-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiInfoDialogComponent {
  dialogData = inject<InfoDialogData>(MAT_DIALOG_DATA);
  dialogRef = inject(MatDialogRef);

  close() {
    this.dialogRef.close();
  }
}
