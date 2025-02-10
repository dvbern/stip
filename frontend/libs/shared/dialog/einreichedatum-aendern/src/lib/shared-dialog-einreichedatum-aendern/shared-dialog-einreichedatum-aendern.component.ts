import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslatePipe } from '@ngx-translate/core';
import { addHours } from 'date-fns';

import { EinreichedatumAendernRequest } from '@dv/shared/model/gesuch';
import { SharedUiFormMessageErrorDirective } from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

export interface EinreichedatumAendernDialogData {
  minDate: string;
  einreichedatum: string;
}

export type EinreichedatumAendernDialogResult = EinreichedatumAendernRequest;

@Component({
  selector: 'dv-shared-dialog-einreichedatum-aendern',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    SharedUiMaxLengthDirective,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './shared-dialog-einreichedatum-aendern.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [provideDvDateAdapter()],
})
export class SharedDialogEinreichedatumAendernComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private dialogRef =
    inject<
      MatDialogRef<
        EinreichedatumAendernDialogData,
        EinreichedatumAendernDialogResult
      >
    >(MatDialogRef);
  dialogData = inject<EinreichedatumAendernDialogData>(MAT_DIALOG_DATA);

  form = this.formBuilder.group({
    einreichedatum: [this.dialogData.einreichedatum],
  });
  maxDate = new Date().toISOString();

  static open(dialog: MatDialog, data: EinreichedatumAendernDialogData) {
    return dialog.open<
      SharedDialogEinreichedatumAendernComponent,
      EinreichedatumAendernDialogData,
      EinreichedatumAendernDialogResult
    >(SharedDialogEinreichedatumAendernComponent, { data });
  }

  close() {
    this.dialogRef.close();
  }

  einreichedatumAendern() {
    if (this.form.invalid) {
      return;
    }

    const { einreichedatum } = convertTempFormToRealValues(this.form);

    this.dialogRef.close({
      newEinreichedatum: normalizeDateForUTC(einreichedatum),
    });
  }
}

const normalizeDateForUTC = (date: Date | string) => {
  const offset = (new Date().getTimezoneOffset() / 60) * -1 + 1;
  return addHours(date, offset).toISOString();
};
