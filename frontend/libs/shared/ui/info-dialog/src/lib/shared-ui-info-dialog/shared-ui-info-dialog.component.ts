import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

export interface InfoDialogData {
  title: string;
  message: string;
}

@Component({
  selector: 'dv-shared-ui-info-dialog',
  standalone: true,
  imports: [CommonModule, TranslateModule],
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
