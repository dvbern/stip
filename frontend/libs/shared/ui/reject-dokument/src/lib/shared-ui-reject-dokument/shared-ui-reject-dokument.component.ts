import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule } from '@ngx-translate/core';

import { TableDocument } from '@dv/shared/pattern/document-upload';
import { SharedUiFormFieldDirective } from '@dv/shared/ui/form';

export interface RejectDocumentDialogData {
  dokument: TableDocument;
}

export interface RejectDokument {
  id: string;
  kommentar: string;
}

@Component({
  selector: 'dv-shared-ui-reject-dokument',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatFormFieldModule,
    MatInputModule,
    SharedUiFormFieldDirective,
    ReactiveFormsModule,
  ],
  templateUrl: './shared-ui-reject-dokument.component.html',
  styleUrl: './shared-ui-reject-dokument.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiRejectDokumentComponent {
  public dialogData = inject<RejectDocumentDialogData>(MAT_DIALOG_DATA);
  private dialogRef = inject(MatDialogRef);
  private formBuilder = inject(NonNullableFormBuilder);

  form = this.formBuilder.group({
    kommentar: [<string | undefined>undefined],
  });

  cancel() {
    this.dialogRef.close({ gigu: 'sdfsdf' });
  }

  rejectDocument() {
    const id = this.dialogData.dokument.id;
    const kommentar = this.form.value.kommentar;

    this.dialogRef.close({ id, kommentar });
  }
}
