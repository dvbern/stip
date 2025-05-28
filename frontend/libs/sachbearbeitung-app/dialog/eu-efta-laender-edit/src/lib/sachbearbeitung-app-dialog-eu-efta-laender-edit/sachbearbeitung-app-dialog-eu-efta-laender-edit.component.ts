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

type EuEftaLandEditData = {
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
    iso3code: [<string | undefined>undefined, [Validators.required]],
    deKurzform: [<string | undefined>undefined, [Validators.required]],
    frKurzform: [<string | undefined>undefined, [Validators.required]],
    itKurzform: [<string | undefined>undefined, [Validators.required]],
    engKurzform: [<string | undefined>undefined, [Validators.required]],
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

    this.dialogRef.close(
      convertTempFormToRealValues(this.form, [
        'laendercodeBfs',
        'iso3code',
        'deKurzform',
        'frKurzform',
        'itKurzform',
        'engKurzform',
      ]),
    );
  }
  close() {
    this.dialogRef.close();
  }
}
