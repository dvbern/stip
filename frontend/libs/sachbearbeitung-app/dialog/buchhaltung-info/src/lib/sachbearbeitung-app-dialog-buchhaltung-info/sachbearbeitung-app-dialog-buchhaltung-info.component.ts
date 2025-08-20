import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslatePipe } from '@ngx-translate/core';

import { BuchhaltungEntry } from '@dv/shared/model/gesuch';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

type BuchhaltungInfoData = BuchhaltungEntry;

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-buchhaltung-info',
  imports: [CommonModule, TranslatePipe, SharedUiFormatChfPipe],
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

  sapDeliverysSig = computed(() => {
    return this.dialogData.sapDeliverys
      ? [...this.dialogData.sapDeliverys].sort((a, b) => {
          return (
            new Date(a.timestampErstellt).getTime() -
            new Date(b.timestampErstellt).getTime()
          );
        })
      : undefined;
  });

  close() {
    this.dialogRef.close();
  }
}
