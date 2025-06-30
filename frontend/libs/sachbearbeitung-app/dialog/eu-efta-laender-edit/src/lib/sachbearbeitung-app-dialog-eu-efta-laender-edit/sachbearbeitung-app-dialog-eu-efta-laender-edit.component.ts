import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatCheckbox } from '@angular/material/checkbox';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MaskitoDirective } from '@maskito/angular';
import { MaskitoOptions } from '@maskito/core';
import { TranslatePipe } from '@ngx-translate/core';

import { Land } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

export type EuEftaLandEditData = {
  laender: Land[];
  land?: Land;
};

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-eu-efta-laender-edit',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    FormsModule,
    MatError,
    MatFormField,
    MatLabel,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    MatInput,
    MatCheckbox,
    MaskitoDirective,
  ],
  templateUrl:
    './sachbearbeitung-app-dialog-eu-efta-laender-edit.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppDialogEuEftaLaenderEditComponent {
  private dialogRef = inject(MatDialogRef);
  dialogData = inject<EuEftaLandEditData>(MAT_DIALOG_DATA);
  private formBuilder = inject(NonNullableFormBuilder);

  // Custom mask for BFS code - limit to exactly 4 digits
  maskitoBfsCode: MaskitoOptions = {
    mask: [/\d/, /\d/, /\d/, /\d/],
  };

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

  // Custom mask for ISO2 code - limit to exactly 2 uppercase letters
  maskitoIso2Code: MaskitoOptions = {
    mask: [/[a-zA-Z]/, /[a-zA-Z]/],
    postprocessors: [
      ({ selection, value }) => {
        return {
          selection,
          value: value.toUpperCase(),
        };
      },
    ],
  };

  // Custom mask for ISO3 code - limit to exactly 3 uppercase letters
  maskitoIso3Code: MaskitoOptions = {
    mask: [/[a-zA-Z]/, /[a-zA-Z]/, /[a-zA-Z]/],
    postprocessors: [
      ({ selection, value }) => {
        return {
          selection,
          value: value.toUpperCase(),
        };
      },
    ],
  };

  uniqueIso2CodeValidator = (): ValidatorFn => (control) => {
    const laender = this.dialogData.laender;
    const currentValue = control.value;
    if (!currentValue) {
      return null;
    }
    const currentEditLand = this.dialogData.land;
    const duplicateExists = laender.some((entry) => {
      const isSameAsCurrent =
        currentEditLand && entry.iso2code === currentEditLand.iso2code;

      return entry.iso2code === currentValue && !isSameAsCurrent;
    });

    return duplicateExists ? { notUniqueIso2: true } : null;
  };

  // validator for ISO3 code to ensure it's unique
  uniqueIso3CodeValidator = (): ValidatorFn => (control) => {
    const laender = this.dialogData.laender;
    const currentValue = control.value;

    if (!currentValue) {
      return null;
    }
    const currentEditLand = this.dialogData.land;

    const duplicateExists = laender.some((entry) => {
      const isSameAsCurrent =
        currentEditLand && entry.iso3code === currentEditLand.iso3code;

      return entry.iso3code === currentValue && !isSameAsCurrent;
    });

    return duplicateExists ? { notUniqueIso3: true } : null;
  };

  static open(dialog: MatDialog, data: EuEftaLandEditData) {
    return dialog.open<
      SachbearbeitungAppDialogEuEftaLaenderEditComponent,
      EuEftaLandEditData,
      Land
    >(SachbearbeitungAppDialogEuEftaLaenderEditComponent, {
      data,
    });
  }

  form = this.formBuilder.group({
    laendercodeBfs: [
      <string | undefined>undefined,
      [Validators.required, this.uniqueBfsNumberValidator()],
    ],
    iso2code: [<string | undefined>undefined, [this.uniqueIso2CodeValidator()]],
    iso3code: [<string | undefined>undefined, [this.uniqueIso3CodeValidator()]],
    deKurzform: [<string | undefined>undefined, [Validators.required]],
    frKurzform: [<string | undefined>undefined, [Validators.required]],
    itKurzform: [<string | undefined>undefined, [Validators.required]],
    enKurzform: [<string | undefined>undefined, [Validators.required]],
    eintragGueltig: [false],
    isEuEfta: [false],
  });

  constructor() {
    if (this.dialogData.land) {
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

    const values = convertTempFormToRealValues(this.form, [
      'laendercodeBfs',
      'deKurzform',
      'frKurzform',
      'itKurzform',
      'enKurzform',
    ]);

    // if iso2code is an empty string, set it to undefined
    if (values.iso2code === '') {
      values.iso2code = undefined;
    }
    // if iso3code is an empty string, set it to undefined
    if (values.iso3code === '') {
      values.iso3code = undefined;
    }

    this.dialogRef.close({ ...this.dialogData.land, ...values } as Land);
  }

  close() {
    this.dialogRef.close();
  }
}
