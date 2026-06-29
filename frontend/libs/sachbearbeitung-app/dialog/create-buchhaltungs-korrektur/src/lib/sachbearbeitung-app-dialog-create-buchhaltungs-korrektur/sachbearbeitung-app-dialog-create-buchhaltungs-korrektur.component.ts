import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  AbstractControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MaskitoDirective } from '@maskito/angular';

import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import { BuchhaltungSaldokorrektur } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoNumberWithNegative,
} from '@dv/shared/util/maskito-util';

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-create-buchhaltungs-korrektur',
  imports: [
    ReactiveFormsModule,
    MaskitoDirective,
    MatFormFieldModule,
    MatInputModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SachbearbeitungAppUiAdvTranslocoDirective,
  ],
  templateUrl:
    './sachbearbeitung-app-dialog-create-buchhaltungs-korrektur.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppDialogCreateBuchhaltungsKorrekturComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private dialogRef = inject(MatDialogRef);
  maskitoNumberWithNegative = maskitoNumberWithNegative;
  form = this.formBuilder.group({
    betrag: [
      <string | null>null,
      [
        Validators.required,
        (control: AbstractControl) =>
          +control.getRawValue() === 0 ? { notZero: true } : null,
      ],
    ],
    comment: [<string | null>null, [Validators.required]],
  });

  static open(dialog: MatDialog) {
    return dialog.open<
      SachbearbeitungAppDialogCreateBuchhaltungsKorrekturComponent,
      undefined,
      BuchhaltungSaldokorrektur
    >(SachbearbeitungAppDialogCreateBuchhaltungsKorrekturComponent);
  }

  returnKorrektur() {
    this.form.markAllAsTouched();

    if (this.form.invalid) {
      return;
    }

    const { betrag, comment } = convertTempFormToRealValues(this.form, [
      'betrag',
      'comment',
    ]);
    this.dialogRef.close({ betrag: fromFormatedNumber(betrag), comment });
  }

  cancel() {
    this.dialogRef.close();
  }
}
