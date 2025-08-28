import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslocoPipe } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import { fromFormatedNumber, maskitoYear } from '@dv/shared/util/maskito-util';

type UpdateSteuerdatenDialogResult = {
  steuerjahr: number;
};

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-update-steuerdaten',
  imports: [
    CommonModule,
    MaskitoDirective,
    MatInputModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    TranslocoPipe,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './sachbearbeitung-app-dialog-update-steuerdaten.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppDialogUpdateSteuerdatenComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private dialogRef =
    inject<
      MatDialogRef<
        SachbearbeitungAppDialogUpdateSteuerdatenComponent,
        UpdateSteuerdatenDialogResult
      >
    >(MatDialogRef);
  maskitoYear = maskitoYear;

  form = this.formBuilder.group({
    steuerjahr: [<string | null>null, [Validators.required]],
  });

  static open(dialog: MatDialog) {
    return dialog.open<
      SachbearbeitungAppDialogUpdateSteuerdatenComponent,
      undefined,
      UpdateSteuerdatenDialogResult
    >(SachbearbeitungAppDialogUpdateSteuerdatenComponent);
  }

  cancel() {
    this.dialogRef.close();
  }

  handleSave() {
    if (!this.form.valid) {
      return;
    }

    const { steuerjahr } = convertTempFormToRealValues(this.form);

    this.dialogRef.close({
      steuerjahr: fromFormatedNumber(steuerjahr),
    });
  }
}
