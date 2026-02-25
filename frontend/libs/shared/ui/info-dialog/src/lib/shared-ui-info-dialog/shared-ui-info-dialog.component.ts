import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

import { TranslocoHashMap } from '@dv/shared/model/type-util';

export interface InfoDialogData {
  titleKey: string;
  titleParams?: TranslocoHashMap;
  messageKey: string;
  messageParams?: TranslocoHashMap;
}

@Component({
  selector: 'dv-shared-ui-info-dialog',
  imports: [TranslocoPipe],
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
