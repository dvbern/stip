import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslocoPipe } from '@jsverse/transloco';

import { RenameAusbildungsstaette } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type EditAusbildungsstaetteData = {
  nameDe: string;
  nameFr: string;
};

@Component({
  selector: 'dv-edit-abschluss-dialog',
  imports: [
    ReactiveFormsModule,
    TranslocoPipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './edit-ausbildungsstaette-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditAusbildungsstaetteDialogComponent {
  private dialogdata = inject<EditAusbildungsstaetteData>(MAT_DIALOG_DATA);
  private dialogRef =
    inject<MatDialogRef<EditAusbildungsstaetteData, RenameAusbildungsstaette>>(
      MatDialogRef,
    );
  private formBuilder = inject(NonNullableFormBuilder);

  form = this.formBuilder.group({
    nameDe: [this.dialogdata.nameDe, Validators.required],
    nameFr: [this.dialogdata.nameFr, Validators.required],
  });

  static open(dialog: MatDialog, data: EditAusbildungsstaetteData) {
    return dialog.open<
      EditAusbildungsstaetteDialogComponent,
      EditAusbildungsstaetteData,
      RenameAusbildungsstaette
    >(EditAusbildungsstaetteDialogComponent, { data });
  }

  confirm() {
    if (this.form.invalid) {
      return;
    }

    const values = convertTempFormToRealValues(this.form);
    this.dialogRef.close(values);
  }

  cancel() {
    this.dialogRef.close();
  }
}
