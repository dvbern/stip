import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { SharedTranslationKey } from '@dv/shared/assets/i18n';

type InfoEntry =
  | {
      type: 'info';
      labelKey: SachbearbeitungAppTranslationKey;
      value: string | number;
    }
  | {
      type: 'translatedInfo';
      labelKey: SachbearbeitungAppTranslationKey;
      valueKey: SharedTranslationKey | SachbearbeitungAppTranslationKey;
    }
  | {
      type: 'spacer';
    };
type InfoData = {
  titleKey: SachbearbeitungAppTranslationKey;
  entries: InfoEntry[];
};

const createInfoData = {
  info: (
    labelKey: SachbearbeitungAppTranslationKey,
    value: string | number,
  ): InfoEntry => ({
    type: 'info',
    labelKey,
    value,
  }),
  translatedInfo: (
    labelKey: SachbearbeitungAppTranslationKey,
    valueKey: SharedTranslationKey | SachbearbeitungAppTranslationKey,
  ): InfoEntry => ({
    type: 'translatedInfo',
    labelKey,
    valueKey,
  }),
  spacer: (): InfoEntry => ({ type: 'spacer' }),
};

@Component({
  selector: 'dv-data-info-dialog',
  imports: [CommonModule, TranslocoPipe, MatCheckboxModule],
  templateUrl: './data-info-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DataInfoDialogComponent {
  private dialogRef = inject(MatDialogRef);
  dialogData = inject<InfoData>(MAT_DIALOG_DATA);

  static open(
    dialog: MatDialog,
    titleKey: SachbearbeitungAppTranslationKey,
    createEntries: (helper: typeof createInfoData) => InfoEntry[],
  ) {
    return dialog.open<DataInfoDialogComponent, InfoData>(
      DataInfoDialogComponent,
      {
        data: {
          titleKey,
          entries: [...createEntries(createInfoData)],
        },
      },
    );
  }

  close() {
    this.dialogRef.close();
  }
}
