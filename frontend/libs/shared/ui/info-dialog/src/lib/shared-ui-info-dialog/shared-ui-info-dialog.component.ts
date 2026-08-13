import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { TranslocoHashMap } from '@dv/shared/model/type-util';

export interface InfoDialogData {
  titleKey: SharedTranslationKey;
  titleParams?: TranslocoHashMap;
  messageKey: SharedTranslationKey;
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

  static open(dialog: MatDialog, data: InfoDialogData) {
    return dialog.open<SharedUiInfoDialogComponent, InfoDialogData, void>(
      SharedUiInfoDialogComponent,
      {
        panelClass: 'dv-info-dialog',
        data,
      },
    );
  }

  close() {
    this.dialogRef.close();
  }
}
