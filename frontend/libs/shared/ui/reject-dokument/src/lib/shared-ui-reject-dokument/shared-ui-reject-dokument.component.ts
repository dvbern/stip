import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslatePipe } from '@ngx-translate/core';

import { SharedModelGesuchDokument } from '@dv/shared/model/dokument';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';

export interface RejectDokument {
  gesuchDokumentId: string;
  kommentar: string;
}

@Component({
  selector: 'dv-shared-ui-reject-dokument',
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
  templateUrl: './shared-ui-reject-dokument.component.html',
  styleUrl: './shared-ui-reject-dokument.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiRejectDokumentComponent {
  private data = inject<SharedModelGesuchDokument>(MAT_DIALOG_DATA);
  private dialogRef =
    inject<MatDialogRef<SharedUiRejectDokumentComponent, RejectDokument>>(
      MatDialogRef,
    );
  private formBuilder = inject(NonNullableFormBuilder);

  form = this.formBuilder.group({
    kommentar: [<string | undefined>undefined, [Validators.required]],
  });

  cancel() {
    this.dialogRef.close();
  }

  rejectDocument() {
    this.form.markAllAsTouched();
    const gesuchDokumentId = this.data.gesuchDokument?.id;
    const kommentar = this.form.value.kommentar;

    if (!gesuchDokumentId || !kommentar)
      throw new Error('Id or Kommentar is missing');

    this.dialogRef.close({ gesuchDokumentId, kommentar });
  }
}
