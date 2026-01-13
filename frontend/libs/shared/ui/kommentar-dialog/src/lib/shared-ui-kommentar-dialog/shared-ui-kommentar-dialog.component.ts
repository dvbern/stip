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
import { TranslocoPipe } from '@jsverse/transloco';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';

export interface KommentarDialogData<T extends string> {
  titleKey: T;
  labelKey?: T;
  messageKey?: T;
  placeholderKey: T;
  confirmKey: T;
}

export interface KommentarDialogResult {
  kommentar: string;
}
export interface OptionalKommentarDialogResult {
  kommentar?: string;
}

@Component({
  selector: 'dv-shared-ui-kommentar-dialog',
  imports: [
    TranslocoPipe,
    MatFormFieldModule,
    MatInputModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiMaxLengthDirective,
    ReactiveFormsModule,
  ],
  templateUrl: './shared-ui-kommentar-dialog.component.html',
  styleUrls: ['./shared-ui-kommentar-dialog.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiKommentarDialogComponent<T extends string> {
  private dialogRef =
    inject<
      MatDialogRef<
        SharedUiKommentarDialogComponent<T>,
        KommentarDialogResult | OptionalKommentarDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  dialogData = inject<KommentarDialogData<T> & { optional: boolean }>(
    MAT_DIALOG_DATA,
  );

  form = this.formBuilder.group({
    kommentar: [<string | undefined>undefined],
  });

  constructor() {
    if (!this.dialogData.optional) {
      this.form.controls.kommentar.addValidators(Validators.required);
    }
  }

  static open<T extends string>(
    dialog: MatDialog,
    data: KommentarDialogData<T>,
  ) {
    return dialog.open<
      SharedUiKommentarDialogComponent<T>,
      KommentarDialogData<T> & { optional: false },
      KommentarDialogResult
    >(SharedUiKommentarDialogComponent, { data: { ...data, optional: false } });
  }

  static openOptional<T extends string>(
    dialog: MatDialog,
    data: KommentarDialogData<T>,
  ) {
    return dialog.open<
      SharedUiKommentarDialogComponent<T>,
      KommentarDialogData<T> & { optional: true },
      OptionalKommentarDialogResult
    >(SharedUiKommentarDialogComponent, { data: { ...data, optional: true } });
  }

  confirm() {
    this.form.markAllAsTouched();
    const kommentar = this.form.value.kommentar;
    if (this.form.invalid) {
      return;
    }
    this.dialogRef.close({ kommentar });
  }

  cancel() {
    this.dialogRef.close();
  }
}
