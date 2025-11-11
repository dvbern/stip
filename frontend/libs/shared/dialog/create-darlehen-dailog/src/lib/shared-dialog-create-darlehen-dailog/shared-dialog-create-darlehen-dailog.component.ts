import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedFeatureDarlehenComponent } from '@dv/shared/feature/darlehen';

export type CreateDarlehenData = {
  gesuchId: string;
};

@Component({
  selector: 'dv-shared-dialog-create-darlehen-dailog',
  imports: [TranslocoPipe, SharedFeatureDarlehenComponent],
  templateUrl: './shared-dialog-create-darlehen-dailog.component.html',
  styleUrl: './shared-dialog-create-darlehen-dailog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogCreateDarlehenDailogComponent {
  private dialogRef = inject(
    MatDialogRef<SharedDialogCreateDarlehenDailogComponent>,
  );

  dialogData = inject<CreateDarlehenData>(MAT_DIALOG_DATA);

  cancel() {
    this.dialogRef.close(false);
  }

  darlehenCreated() {
    this.dialogRef.close(true);
  }

  static open(matDialog: MatDialog, gesuchId: string) {
    return matDialog.open<
      SharedDialogCreateDarlehenDailogComponent,
      CreateDarlehenData
    >(SharedDialogCreateDarlehenDailogComponent, {
      panelClass: 'dv-create-darlehen-dialog',
      data: { gesuchId },
    });
  }
}
