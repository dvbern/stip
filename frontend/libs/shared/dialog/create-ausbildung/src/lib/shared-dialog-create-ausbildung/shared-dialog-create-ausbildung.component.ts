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
  minAusbildungEnd: string | undefined;
};

export type CreateAusbildungResult = {
  gesuchId: string;
  gesuchTrancheId: string;
} | null;

@Component({
  selector: 'dv-shared-dialog-create-ausbildung',
  imports: [TranslocoPipe, SharedFeatureAusbildungComponent],
  templateUrl: './shared-dialog-create-ausbildung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogCreateAusbildungComponent {
  private dialogRef =
    inject<
      MatDialogRef<
        SharedDialogCreateAusbildungComponent,
        CreateAusbildungResult
      >
    >(MatDialogRef);
  dialogData = inject<CreateAusbildungData>(MAT_DIALOG_DATA);

  cancel() {
    this.dialogRef.close(null);
  }

  savedSuccess(result: { gesuchId: string; gesuchTrancheId: string }) {
    this.dialogRef.close(result);
  }

  static open(
    matDialog: MatDialog,
    fallId: string,
    minAusbildungEnd: string | undefined,
  ) {
    return matDialog.open<
      SharedDialogCreateAusbildungComponent,
      CreateAusbildungData,
      CreateAusbildungResult
    >(SharedDialogCreateAusbildungComponent, {
      panelClass: 'dv-dialog-formular',
      data: { fallId, minAusbildungEnd },
    });
  }
}
