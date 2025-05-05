import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  inject,
  viewChild,
} from '@angular/core';
import {
  FormsModule,
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
import { MatRadioButton, MatRadioGroup } from '@angular/material/radio';
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

export type BeschwerdeentscheidUploadDialogData = {
  allowedTypes: string[] | undefined;
};

export interface BeschwerdeentscheidUploadDialogResult {
  fileUpload: File;
  kommentar: string;
  beschwerdeErfolgreich: boolean;
}

@Component({
  selector: 'dv-sachbearbeitung-app-dialog-beschwaerde-entscheid',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    MatRadioButton,
    MatRadioGroup,
    SharedUiFormFieldDirective,
  ],
  templateUrl:
    './sachbearbeitung-app-dialog-beschwaerde-entscheid.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppDialogBeschwaerdeEntscheidComponent {
  private dialogRef =
    inject<
      MatDialogRef<
        SachbearbeitungAppDialogBeschwaerdeEntscheidComponent,
        BeschwerdeentscheidUploadDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);

  dialogData = inject<BeschwerdeentscheidUploadDialogData>(MAT_DIALOG_DATA);
  fileInputSig = viewChild<ElementRef<HTMLInputElement>>('fileInput');
  form = this.formBuilder.group({
    fileUpload: [<File | undefined>undefined, Validators.required],
    kommentar: [<string | null>null, [Validators.required]],
    beschwerdeErfolgreich: [<boolean | null>null, Validators.required],
  });

  static open(dialog: MatDialog, data: BeschwerdeentscheidUploadDialogData) {
    return dialog.open<
      SachbearbeitungAppDialogBeschwaerdeEntscheidComponent,
      BeschwerdeentscheidUploadDialogData,
      BeschwerdeentscheidUploadDialogResult
    >(SachbearbeitungAppDialogBeschwaerdeEntscheidComponent, { data });
  }

  confirm() {
    this.form.markAllAsTouched();
    const fileUpload = this.fileInputSig()?.nativeElement?.files?.[0];

    if (this.form.invalid || !fileUpload) {
      return;
    }

    const formValues = convertTempFormToRealValues(this.form);

    this.dialogRef.close({
      fileUpload,
      kommentar: formValues.kommentar,
      beschwerdeErfolgreich: formValues.beschwerdeErfolgreich,
    });
  }

  close() {
    this.dialogRef.close();
  }
}
