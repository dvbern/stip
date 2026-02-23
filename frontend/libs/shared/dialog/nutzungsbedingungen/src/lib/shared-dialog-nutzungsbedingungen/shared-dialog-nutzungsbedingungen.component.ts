import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

@Component({
  selector: 'dv-shared-dialog-nutzungsbedingungen',
  imports: [TranslocoPipe],
  templateUrl: './shared-dialog-nutzungsbedingungen.component.html',
  styles: ``,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogNutzungsbedingungenComponent {
  private dialogRef = inject(MatDialogRef);

  cancel() {
    this.dialogRef.close(false);
  }

  accept() {
    this.dialogRef.close(true);
  }

  static open(matDialog: MatDialog) {
    return matDialog.open<SharedDialogNutzungsbedingungenComponent>(
      SharedDialogNutzungsbedingungenComponent,
      {
        panelClass: 'dv-user-consent-dialog',
        width: '800px',
      },
    );
  }
}
