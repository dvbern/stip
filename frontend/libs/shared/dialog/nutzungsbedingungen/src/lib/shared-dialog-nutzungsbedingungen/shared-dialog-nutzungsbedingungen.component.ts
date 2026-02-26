import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';

import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';

export type NutzungsbedingungenDialogData = {
  nutzungsbedingungenAkzeptiert: boolean;
};

@Component({
  selector: 'dv-shared-dialog-nutzungsbedingungen',
  imports: [SharedUiAdvTranslocoDirective],
  templateUrl: './shared-dialog-nutzungsbedingungen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogNutzungsbedingungenComponent {
  private dialogRef = inject(MatDialogRef);
  dialogData = inject<NutzungsbedingungenDialogData>(MAT_DIALOG_DATA);

  cancel() {
    this.dialogRef.close(false);
  }

  accept() {
    this.dialogRef.close(true);
  }

  static open(matDialog: MatDialog, nutzungsbedingungenAkzeptiert: boolean) {
    return matDialog.open<
      SharedDialogNutzungsbedingungenComponent,
      NutzungsbedingungenDialogData,
      boolean
    >(SharedDialogNutzungsbedingungenComponent, {
      panelClass: 'dv-user-consent-dialog',
      width: '800px',
      data: { nutzungsbedingungenAkzeptiert },
    });
  }
}
