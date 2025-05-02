import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  computed,
  inject,
  viewChild,
} from '@angular/core';
import {
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioButton, MatRadioGroup } from '@angular/material/radio';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { SharedPatternDocumentUploadComponent } from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

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
    SharedPatternDocumentUploadComponent,
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
  private store = inject(Store);
  private dialogRef =
    inject<
      MatDialogRef<
        SachbearbeitungAppDialogBeschwaerdeEntscheidComponent,
        BeschwerdeentscheidUploadDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  private deploymentConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );

  fileInputSig = viewChild<ElementRef<HTMLInputElement>>('fileInput');
  allowTypesSig = computed(() => {
    return this.deploymentConfigSig().deploymentConfig?.allowedMimeTypes;
  });
  form = this.formBuilder.group({
    fileUpload: [<File | undefined>undefined, Validators.required],
    kommentar: [
      <string | null>null,
      [Validators.required, Validators.maxLength(2000)],
    ],
    beschwerdeErfolgreich: [<boolean | null>null, Validators.required],
  });

  static open(dialog: MatDialog) {
    return dialog.open<
      SachbearbeitungAppDialogBeschwaerdeEntscheidComponent,
      undefined,
      BeschwerdeentscheidUploadDialogResult
    >(SachbearbeitungAppDialogBeschwaerdeEntscheidComponent);
  }

  confirm() {
    this.form.markAllAsTouched();
    const fileUpload = this.fileInputSig()?.nativeElement?.files?.[0];
    console.log('errors?', {
      form: this.form,
      errors: this.form.errors,
      file: fileUpload,
      input: this.fileInputSig(),
    });

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
