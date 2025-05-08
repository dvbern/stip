import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslatePipe } from '@ngx-translate/core';

import { BeschwerdeVerlaufEntry } from '@dv/shared/model/gesuch';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-beschwerde-entry',
  standalone: true,
  imports: [CommonModule, TranslatePipe, SharedUiDownloadButtonDirective],
  templateUrl: './sachbearbeitung-app-dialog-beschwerde-entry.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppDialogBeschwerdeEntryComponent {
  private dialogRef = inject(MatDialogRef);
  dialogData = inject<BeschwerdeVerlaufEntry>(MAT_DIALOG_DATA);

  static open(dialog: MatDialog, entry: BeschwerdeVerlaufEntry) {
    return dialog
      .open<
        SachbearbeitungAppDialogBeschwerdeEntryComponent,
        BeschwerdeVerlaufEntry
      >(SachbearbeitungAppDialogBeschwerdeEntryComponent, {
        data: entry,
      })
      .afterClosed();
  }

  close() {
    this.dialogRef.close();
  }
}
