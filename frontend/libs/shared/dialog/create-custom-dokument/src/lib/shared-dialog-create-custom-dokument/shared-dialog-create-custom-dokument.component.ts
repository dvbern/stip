import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  AbstractControl,
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

import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';

export type DialogOptions = {
  hideDescription?: boolean;
  alreadyUsedTypes?: string[];
};

export type CustomDokumentDialogResult = {
  name: string;
  kommentar: string;
};

@Component({
  selector: 'dv-shared-dialog-create-custom-dokument',
  imports: [
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './shared-dialog-create-custom-dokument.component.html',
  styleUrl: './shared-dialog-create-custom-dokument.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedDialogCreateCustomDokumentComponent {
  private dialogRef =
    inject<
      MatDialogRef<
        SharedDialogCreateCustomDokumentComponent,
        CustomDokumentDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  data = inject<DialogOptions>(MAT_DIALOG_DATA, { optional: true });

  static open(dialog: MatDialog, options?: DialogOptions) {
    return dialog.open<
      SharedDialogCreateCustomDokumentComponent,
      DialogOptions,
      CustomDokumentDialogResult
    >(SharedDialogCreateCustomDokumentComponent, { data: options });
  }

  form = this.formBuilder.group({
    name: [
      '',
      [Validators.required, uniqueValidator(this.data?.alreadyUsedTypes ?? [])],
    ],
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

const uniqueValidator =
  (allowed: string[]) => (control: AbstractControl<string>) => {
    if (allowed.includes(control.value)) {
      return {
        notUnique: true,
      };
    }
    return null;
  };
