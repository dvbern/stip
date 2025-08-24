import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

import { BuchhaltungEntry } from '@dv/shared/model/gesuch';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

type BuchhaltungInfoData = BuchhaltungEntry;

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-buchhaltung-info',
  imports: [CommonModule, TranslocoPipe, SharedUiFormatChfPipe],
  templateUrl: './sachbearbeitung-app-dialog-buchhaltung-info.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppDialogBuchhaltungInfoComponent {
  private dialogRef = inject(MatDialogRef);
  dialogData = inject<BuchhaltungInfoData>(MAT_DIALOG_DATA);

  static open(dialog: MatDialog, entry: BuchhaltungEntry) {
    return dialog.open<
      SachbearbeitungAppDialogBuchhaltungInfoComponent,
      BuchhaltungInfoData
    >(SachbearbeitungAppDialogBuchhaltungInfoComponent, {
      data: entry,
    });
  }

  close() {
    this.dialogRef.close();
  }
}
