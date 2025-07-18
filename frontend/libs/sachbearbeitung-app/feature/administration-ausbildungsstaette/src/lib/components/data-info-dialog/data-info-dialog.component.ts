import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatCheckboxModule } from '@angular/material/checkbox';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslatePipe } from '@ngx-translate/core';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';

type InfoEntry =
  | {
      type: 'info';
      labelKey: SachbearbeitungAppTranslationKey;
      value: string | number;
    }
  | {
      type: 'translatedInfo';
      labelKey: SachbearbeitungAppTranslationKey;
      valueKey: SachbearbeitungAppTranslationKey;
    }
  | {
      type: 'spacer';
    };
type InfoData = {
  titleKey: SachbearbeitungAppTranslationKey;
  entries: InfoEntry[];
};

const createInfoData = <T extends object>(entry: T) => ({
  info: (
    labelKey: SachbearbeitungAppTranslationKey,
    value: keyof T,
  ): InfoEntry => ({
    type: 'info',
    labelKey,
    value: entry[value] as string | number,
  }),
  translatedInfo: (
    labelKey: SachbearbeitungAppTranslationKey,
    valueKey: SachbearbeitungAppTranslationKey,
  ): InfoEntry => ({
    type: 'translatedInfo',
    labelKey,
    valueKey,
  }),
  spacer: (): InfoEntry => ({ type: 'spacer' }),
});

@Component({
  selector: 'dv-data-info-dialog',
  imports: [CommonModule, TranslatePipe, MatCheckboxModule],
  templateUrl: './data-info-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DataInfoDialogComponent {
  private dialogRef = inject(MatDialogRef);
  dialogData = inject<InfoData>(MAT_DIALOG_DATA);

  static open<T extends object>(
    dialog: MatDialog,
    titleKey: SachbearbeitungAppTranslationKey,
    entry: T,
    entries: (helper: ReturnType<typeof createInfoData<T>>) => InfoEntry[],
  ) {
    return dialog.open<DataInfoDialogComponent, InfoData>(
      DataInfoDialogComponent,
      {
        data: {
          titleKey,
          entries: [...entries(createInfoData(entry))],
        },
      },
    );
  }

  close() {
    this.dialogRef.close();
  }
}
