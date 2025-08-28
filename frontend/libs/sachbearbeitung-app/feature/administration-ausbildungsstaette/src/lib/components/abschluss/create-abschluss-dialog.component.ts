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
import { MatSelectModule } from '@angular/material/select';
import { TranslocoPipe } from '@jsverse/transloco';

import {
  Bildungsrichtung,
  BrueckenangebotCreate,
} from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

const invalidBildungsrichtungen: Bildungsrichtung[] = ['OBLIGATORISCHE_SCHULE'];

@Component({
  selector: 'dv-create-abschluss-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslocoPipe,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
  ],
  templateUrl: './create-abschluss-dialog.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CreateAbschlussDialogComponent {
  private dialogRef =
    inject<MatDialogRef<void, BrueckenangebotCreate>>(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);

  bildungsrichtungen = Object.values(Bildungsrichtung).filter(
    (richtung) => !invalidBildungsrichtungen.includes(richtung),
  );
  form = this.formBuilder.group({
    bildungsrichtung: [<Bildungsrichtung | null>null, Validators.required],
    bezeichnungDe: [<string | null>null, Validators.required],
    bezeichnungFr: [<string | null>null, Validators.required],
  });

  static open(dialog: MatDialog) {
    return dialog.open<
      CreateAbschlussDialogComponent,
      void,
      BrueckenangebotCreate
    >(CreateAbschlussDialogComponent);
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
