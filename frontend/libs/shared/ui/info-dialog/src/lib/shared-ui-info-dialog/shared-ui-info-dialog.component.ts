import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogConfig,
  MatDialogRef,
} from '@angular/material/dialog';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { TranslocoHashMap } from '@dv/shared/model/type-util';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';

export type InfoDialogData =
  | {
      type: 'translated';
      titleKey: SharedTranslationKey;
      titleParams?: TranslocoHashMap;
      messageKey: SharedTranslationKey;
      messageParams?: TranslocoHashMap;
    }
  | {
      type: 'plain';
      title: string;
      message: string;
    };

@Component({
  selector: 'dv-shared-ui-info-dialog',
  imports: [SharedUiAdvTranslocoDirective],
  templateUrl: './shared-ui-info-dialog.component.html',
  styleUrl: './shared-ui-info-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiInfoDialogComponent {
  dialogData = inject<InfoDialogData>(MAT_DIALOG_DATA);
  dialogRef = inject(MatDialogRef);

  static open(
    dialog: MatDialog,
    dialogConfig: MatDialogConfig<InfoDialogData>,
  ) {
    return dialog.open<SharedUiInfoDialogComponent, InfoDialogData, void>(
      SharedUiInfoDialogComponent,
      {
        ...dialogConfig,
        panelClass: 'dv-info-dialog',
      },
    );
  }

  close() {
    this.dialogRef.close();
  }
}
