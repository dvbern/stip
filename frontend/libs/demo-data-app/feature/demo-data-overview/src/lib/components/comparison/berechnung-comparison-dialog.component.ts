import { Component, inject } from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { IChange } from 'json-diff-ts';

import { DemoDataAppUiAdvTranslocoDirective } from '@dv/demo-data-app/ui/adv-transloco-directive';
import { DemoDataTestBerechnungResult } from '@dv/shared/model/gesuch';

import { ComparisonValueComponent } from './comparison-value.component';

export type BerechnungComparison = (IChange & {
  berechnung: DemoDataTestBerechnungResult | null;
})[];

@Component({
  templateUrl: './berechnung-comparison-dialog.component.html',
  imports: [
    ComparisonValueComponent,
    DemoDataAppUiAdvTranslocoDirective,
    MatTooltipModule,
  ],
})
export class BerechnungComparisonDialogComponent {
  private dialogRef = inject(MatDialogRef);
  data = inject<BerechnungComparison>(MAT_DIALOG_DATA);

  static open(dialog: MatDialog, data: BerechnungComparison) {
    return dialog
      .open<BerechnungComparisonDialogComponent, BerechnungComparison>(
        BerechnungComparisonDialogComponent,
        {
          data,
          minWidth: '500px',
        },
      )
      .afterClosed();
  }

  close() {
    this.dialogRef.close();
  }
}
