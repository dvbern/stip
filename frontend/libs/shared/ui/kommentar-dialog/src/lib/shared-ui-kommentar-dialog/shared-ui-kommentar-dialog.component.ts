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

@Component({
  selector: 'lib-shared-ui-kommentar-dialog',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatFormFieldModule,
    MatInputModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    ReactiveFormsModule,
  ],
  templateUrl: './shared-ui-kommentar-dialog.component.html',
  styleUrls: ['./shared-ui-kommentar-dialog.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiKommentarDialogComponent {
  private dialogRef =
    inject<
      MatDialogRef<SharedUiKommentarDialogComponent, KommentarDialogResult>
    >(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);
  dialogData = inject<KommentarDialogData>(MAT_DIALOG_DATA);

  form = this.formBuilder.group({
    kommentar: [<string | undefined>undefined, [Validators.required]],
  });

  static open(dialog: MatDialog, data: KommentarDialogData) {
    return dialog.open<
      SharedUiKommentarDialogComponent,
      KommentarDialogData,
      KommentarDialogResult
    >(SharedUiKommentarDialogComponent, { data });
  }

  confirm() {
    this.form.markAllAsTouched();
    const entityId = this.dialogData.entityId;
    const kommentar = this.form.value.kommentar;
    if (!entityId || !kommentar) {
      return;
    }
    this.dialogRef.close({ entityId, kommentar });
  }

  cancel() {
    this.dialogRef.close();
  }
}
