import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  standalone: true,
  templateUrl: './confirm-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [TranslateModule],
})
export class SharedPatternConfirmDialogComponent {
  private dialogRef = inject(MatDialogRef);

  confirm() {
    return this.dialogRef.close(true);
  }
  cancel() {
    return this.dialogRef.close(false);
  }
}
