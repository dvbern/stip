import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslocoPipe } from '@jsverse/transloco';

import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';

export interface RejectDokument {
  kommentar: string;
}

@Component({
  selector: 'dv-shared-ui-reject-dokument',
  imports: [
    CommonModule,
    TranslocoPipe,
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
    const kommentar = this.form.value.kommentar;

    if (!kommentar) throw new Error('Kommentar is missing');

    this.dialogRef.close({ kommentar });
  }
}
