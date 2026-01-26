import { A11yModule } from '@angular/cdk/a11y';
import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
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
import { MatRadioModule } from '@angular/material/radio';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedUiFileUploadComponent } from '@dv/shared/ui/file-upload';
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
  imports: [
    TranslocoPipe,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatRadioModule,
    A11yModule,
    ReactiveFormsModule,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    SharedUiFormFieldDirective,
    SharedUiFileUploadComponent,
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
  form = this.formBuilder.group({
    fileUpload: [<File | undefined>undefined, Validators.required],
    kommentar: [<string | null>null, [Validators.required]],
    beschwerdeErfolgreich: [<boolean | null>null, Validators.required],
  });
  selectedFileSig = signal<File | null>(null);

  static open(dialog: MatDialog, data: BeschwerdeentscheidUploadDialogData) {
    return dialog.open<
      SachbearbeitungAppDialogBeschwaerdeEntscheidComponent,
      BeschwerdeentscheidUploadDialogData,
      BeschwerdeentscheidUploadDialogResult
    >(SachbearbeitungAppDialogBeschwaerdeEntscheidComponent, { data });
  }

  confirm() {
    this.form.markAllAsTouched();
    const fileUpload = this.selectedFileSig();

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
