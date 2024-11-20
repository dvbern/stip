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

import { GesuchDokument } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';

export interface RejectDokument {
  id: string;
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
    ReactiveFormsModule,
  ],
  templateUrl: './shared-ui-reject-dokument.component.html',
  styleUrl: './shared-ui-reject-dokument.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiRejectDokumentComponent {
  public gesuchDokument = inject<GesuchDokument>(MAT_DIALOG_DATA);
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
    const id = this.gesuchDokument.id;
    const kommentar = this.form.value.kommentar;

    if (!id || !kommentar) throw new Error('Id or Kommentar is missing');

    this.dialogRef.close({ id, kommentar });
  }
}
