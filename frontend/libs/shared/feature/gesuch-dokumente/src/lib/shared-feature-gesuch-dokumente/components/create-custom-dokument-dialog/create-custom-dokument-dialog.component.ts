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
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';

export type CustomDokumentDialogResult = {
  name: string;
  kommentar: string;
};

@Component({
  selector: 'dv-create-custom-dokument-dialog',
  imports: [
    CommonModule,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    ReactiveFormsModule,
  ],
  templateUrl: './create-custom-dokument-dialog.component.html',
  styleUrl: './create-custom-dokument-dialog.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CreateCustomDokumentDialogComponent {
  private dialogRef =
    inject<
      MatDialogRef<
        CreateCustomDokumentDialogComponent,
        CustomDokumentDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);

  static open(dialog: MatDialog) {
    return dialog.open<
      CreateCustomDokumentDialogComponent,
      CustomDokumentDialogResult
    >(CreateCustomDokumentDialogComponent);
  }

  form = this.formBuilder.group({
    name: ['', [Validators.required]],
    kommentar: ['', [Validators.required]],
  });

  confirm() {
    this.form.markAllAsTouched();
    if (this.form.valid) {
      this.dialogRef.close(this.form.getRawValue());
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}
