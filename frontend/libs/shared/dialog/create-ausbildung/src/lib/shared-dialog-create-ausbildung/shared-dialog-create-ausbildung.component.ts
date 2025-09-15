import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedFeatureAusbildungComponent } from '@dv/shared/feature/ausbildung';

export type CreateAusbildungData = {
  fallId: string;
};

@Component({
  selector: 'dv-shared-dialog-create-ausbildung',
  imports: [TranslocoPipe, SharedFeatureAusbildungComponent],
  templateUrl: './shared-dialog-create-ausbildung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogCreateAusbildungComponent {
  private dialogRef = inject(MatDialogRef);
  dialogData = inject<CreateAusbildungData>(MAT_DIALOG_DATA);

  cancel() {
    this.dialogRef.close(false);
  }

  savedSuccess() {
    this.dialogRef.close(true);
  }

  static open(matDialog: MatDialog, fallId: string) {
    return matDialog.open<
      SharedDialogCreateAusbildungComponent,
      CreateAusbildungData
    >(SharedDialogCreateAusbildungComponent, {
      panelClass: 'dv-create-ausbildung-dialog',
      data: { fallId },
    });
  }
}
