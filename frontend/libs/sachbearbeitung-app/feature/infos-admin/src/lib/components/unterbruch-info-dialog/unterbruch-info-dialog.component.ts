import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  computed,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { addDays } from 'date-fns';

import { SachbearbeitungAppUiAdvTranslocoDirective } from '@dv/sachbearbeitung-app/ui/adv-transloco-directive';
import {
  AusbildungUnterbruchAntragStatus,
  UpdateAusbildungUnterbruchAntragSB,
} from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedPatternDocumentUploadComponent } from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { toBackendLocalDate } from '@dv/shared/util/validator-date';

import { AusbildungUnterbruchAntragEntry } from '../../types';

type TargetStatus = Extract<
  AusbildungUnterbruchAntragStatus,
  'AKZEPTIERT' | 'ABGELEHNT'
>;
type DialogResult = {
  data: UpdateAusbildungUnterbruchAntragSB;
  status: TargetStatus;
};

@Component({
  selector: 'dv-data-info-dialog',
  imports: [
    DatePipe,
    ReactiveFormsModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    SharedUiMaxLengthDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedPatternDocumentUploadComponent,
    SachbearbeitungAppUiAdvTranslocoDirective,
  ],
  providers: [provideDvDateAdapter()],
  templateUrl: './unterbruch-info-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UnterbruchInfoDialogComponent {
  private dialogRef = inject(MatDialogRef);
  private elementRef = inject(ElementRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  dialogData = inject<AusbildungUnterbruchAntragEntry>(MAT_DIALOG_DATA);

  monatValues = new Array(13).fill(0).map((_, index) => index);

  form = this.formBuilder.group({
    startDate: [<string>this.dialogData.startDate, Validators.required],
    endDate: [<string>this.dialogData.endDate, Validators.required],
    kommentarSB: [<string | undefined>undefined, Validators.required],
    monateOhneAnspruch: [<number | undefined>undefined],
  });

  private startDateChangedSig = toSignal(
    this.form.controls.startDate.valueChanges,
    {
      initialValue: this.form.controls.startDate.value,
    },
  );
  oneDayAfterStartDateSig = computed(() => {
    const gueltigAb = this.startDateChangedSig();
    if (!gueltigAb) {
      return null;
    }
    return addDays(gueltigAb, 1);
  });

  static open(dialog: MatDialog, data: AusbildungUnterbruchAntragEntry) {
    return dialog.open<
      UnterbruchInfoDialogComponent,
      AusbildungUnterbruchAntragEntry,
      DialogResult | undefined
    >(UnterbruchInfoDialogComponent, {
      data,
      panelClass: 'dv-dialog-formular',
    });
  }

  update(status: TargetStatus) {
    this.form.markAllAsTouched();
    if (!isDefined(this.form.controls.monateOhneAnspruch.value)) {
      this.form.controls.monateOhneAnspruch.setErrors(
        status === 'AKZEPTIERT' ? { required: true } : null,
      );
    }
    this.formUtils.focusFirstInvalid(this.elementRef);
    if (this.form.invalid) {
      return;
    }
    const values = convertTempFormToRealValues(this.form);

    this.dialogRef.close({
      data: {
        ...values,
        startDate: toBackendLocalDate(values.startDate),
        endDate: toBackendLocalDate(values.endDate),
        status,
      },
      status,
    });
  }

  close() {
    this.dialogRef.close();
  }
}
