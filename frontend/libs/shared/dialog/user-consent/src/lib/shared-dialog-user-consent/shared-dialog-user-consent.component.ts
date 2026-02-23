import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslocoPipe } from '@jsverse/transloco';

@Component({
  selector: 'dv-shared-dialog-user-consent',
  imports: [TranslocoPipe],
  templateUrl: './shared-dialog-user-consent.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogUserConsentComponent {
  private dialogRef = inject(MatDialogRef);

  cancel() {
    this.dialogRef.close(false);
  }

  accept() {
    this.dialogRef.close(true);
  }

  static open(matDialog: MatDialog) {
    return matDialog.open<SharedDialogUserConsentComponent>(
      SharedDialogUserConsentComponent,
      {
        panelClass: 'dv-user-consent-dialog',
        width: '800px',
      },
    );
  }
}
