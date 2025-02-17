import { CommonModule } from '@angular/common';
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
import { TranslatePipe } from '@ngx-translate/core';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';

export interface KommentarDialogData {
  entityId: string;
  titleKey: string;
  labelKey?: string;
  messageKey: string;
  placeholderKey: string;
  confirmKey: string;
}

export interface KommentarDialogResult {
  entityId: string;
  kommentar: string;
}
export interface OptionalKommentarDialogResult {
  entityId: string;
  kommentar?: string;
}

@Component({
  selector: 'dv-shared-ui-kommentar-dialog',
  standalone: true,
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
  templateUrl: './shared-ui-kommentar-dialog.component.html',
  styleUrls: ['./shared-ui-kommentar-dialog.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiKommentarDialogComponent {
  private dialogRef =
    inject<
      MatDialogRef<
        SharedUiKommentarDialogComponent,
        KommentarDialogResult | OptionalKommentarDialogResult
      >
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  dialogData = inject<KommentarDialogData & { optional: boolean }>(
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

  static open(dialog: MatDialog, data: KommentarDialogData) {
    return dialog.open<
      SharedUiKommentarDialogComponent,
      KommentarDialogData & { optional: false },
      KommentarDialogResult
    >(SharedUiKommentarDialogComponent, { data: { ...data, optional: false } });
  }

  static openOptional(dialog: MatDialog, data: KommentarDialogData) {
    return dialog.open<
      SharedUiKommentarDialogComponent,
      KommentarDialogData & { optional: true },
      OptionalKommentarDialogResult
    >(SharedUiKommentarDialogComponent, { data: { ...data, optional: true } });
  }

  confirm() {
    this.form.markAllAsTouched();
    const entityId = this.dialogData.entityId;
    const kommentar = this.form.value.kommentar;
    if (!entityId || this.form.invalid) {
      return;
    }
    this.dialogRef.close({ entityId, kommentar });
  }

  cancel() {
    this.dialogRef.close();
  }
}
