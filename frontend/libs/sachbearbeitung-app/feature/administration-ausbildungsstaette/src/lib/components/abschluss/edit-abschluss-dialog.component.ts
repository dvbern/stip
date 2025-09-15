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

import { RenameAbschluss } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

type EditAbschlussData = {
  bezeichnungDe: string;
  bezeichnungFr: string;
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
  templateUrl: './edit-abschluss-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class EditAbschlussDialogComponent {
  private dialogdata = inject<EditAbschlussData>(MAT_DIALOG_DATA);
  private dialogRef =
    inject<MatDialogRef<EditAbschlussData, RenameAbschluss>>(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);

  form = this.formBuilder.group({
    bezeichnungDe: [this.dialogdata.bezeichnungDe, Validators.required],
    bezeichnungFr: [this.dialogdata.bezeichnungFr, Validators.required],
  });

  static open(dialog: MatDialog, data: EditAbschlussData) {
    return dialog.open<
      EditAbschlussDialogComponent,
      EditAbschlussData,
      RenameAbschluss
    >(EditAbschlussDialogComponent, { data });
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
