import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { TranslatePipe } from '@ngx-translate/core';

import { SharedFeatureAusbildungComponent } from '@dv/shared/feature/ausbildung';

export type CreateAusbildungData = {
  fallId: string;
};

@Component({
  selector: 'dv-gesuch-app-dialog-create-ausbildung',
  standalone: true,
  imports: [CommonModule, TranslatePipe, SharedFeatureAusbildungComponent],
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
      data: { fallId },
    });
  }
}
