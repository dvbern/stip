import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

type CreateAusbildungData = {
  fallId: string;
};

@Component({
  selector: 'dv-gesuch-app-ui-create-ausbildung-dialog',
  standalone: true,
  imports: [CommonModule, TranslateModule, SharedFeatureGesuchFormEducationComponent],
  templateUrl: './gesuch-app-ui-create-ausbildung-dialog.component.html',
  styleUrl: './gesuch-app-ui-create-ausbildung-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppUiCreateAusbildungDialogComponent {
  private dialogRef = inject(MatDialogRef);

  cancel() {
    this.dialogRef.close();
  }

  static open(matDialog: MatDialog, fallId: string): void {
    matDialog.open<
      GesuchAppUiCreateAusbildungDialogComponent,
      CreateAusbildungData
    >(GesuchAppUiCreateAusbildungDialogComponent, {
      data: { fallId },
    });
  }
}
