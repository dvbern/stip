import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatOption } from '@angular/material/autocomplete';
import { MatCheckbox } from '@angular/material/checkbox';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { TranslatePipe } from '@ngx-translate/core';

import { LandEuEfta } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type EuEftaLandEditData = {
  laender: LandEuEfta[];
  land?: LandEuEfta;
};

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-eu-efta-laender-edit',
  standalone: true,
  imports: [
    CommonModule,
    SharedUiFormatChfPipe,
    TranslatePipe,
    FormsModule,
    MatError,
    MatFormField,
    MatLabel,
    MatOption,
    MatSelect,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    MatInput,
    MatCheckbox,
  ],
  templateUrl:
    './sachbearbeitung-app-dialog-eu-efta-laender-edit.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppDialogEuEftaLaenderEditComponent {
  private dialogRef = inject(MatDialogRef);
  dialogData = inject<EuEftaLandEditData>(MAT_DIALOG_DATA);
  private formBuilder = inject(NonNullableFormBuilder);

  isEdit = !!this.dialogData.land?.id;

  uniqueBfsNumberValidator = (): ValidatorFn => (control) => {
    const laender = this.dialogData.laender;
    const currentValue = control.value;

    if (!currentValue) {
      return null;
    }
    const currentEditLand = this.dialogData.land;

    const duplicateExists = laender.some((entry) => {
      const isSameAsCurrent =
        currentEditLand &&
        entry.laendercodeBfs === currentEditLand.laendercodeBfs;

      return entry.laendercodeBfs === currentValue && !isSameAsCurrent;
    });

    return duplicateExists ? { notUniqueBfs: true } : null;
  };

  static open(dialog: MatDialog, data: EuEftaLandEditData) {
    return dialog.open<
      SachbearbeitungAppDialogEuEftaLaenderEditComponent,
      EuEftaLandEditData,
      LandEuEfta
    >(SachbearbeitungAppDialogEuEftaLaenderEditComponent, {
      data,
    });
  }

  form = this.formBuilder.group({
    laendercodeBfs: [
      '',
      [Validators.required, this.uniqueBfsNumberValidator()],
    ],
    iso3code: ['', [Validators.required]],
    deKurzform: ['', [Validators.required]],
    frKurzform: ['', [Validators.required]],
    itKurzform: ['', [Validators.required]],
    engKurzform: ['', [Validators.required]],
    eintragGueltig: [false],
    isEuEfta: [false],
  });

  constructor() {
    if (this.isEdit) {
      this.form.patchValue({
        ...this.dialogData.land,
      });
    }
  }

  confirm() {
    this.form.markAllAsTouched();

    if (!this.form.valid) {
      return;
    }

    const euEftaLaenderDaten = convertTempFormToRealValues(this.form);

    this.dialogRef.close(euEftaLaenderDaten);
  }
  close() {
    this.dialogRef.close();
  }
}
