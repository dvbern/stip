import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiDropFileComponent } from '@dv/shared/ui/drop-file';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

import { SharedPatternDocumentUploadDialogComponent } from '../document-upload-dialog/document-upload-dialog.component';
import { DocumentOptions } from '../upload.model';
import { UploadStore } from '../upload.store';

type DialogType = SharedPatternDocumentUploadDialogComponent;
type DialogData = SharedPatternDocumentUploadDialogComponent['data'];

@Component({
  selector: 'dv-shared-pattern-document-upload',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatDialogModule,
    MatFormFieldModule,
    SharedUiDropFileComponent,
    SharedUiIconChipComponent,
  ],
  templateUrl: './document-upload.component.html',
  styleUrls: ['./document-upload.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [UploadStore],
})
export class SharedPatternDocumentUploadComponent {
  private dialog = inject(MatDialog);
  private store = inject(UploadStore);
  optionsSig = input.required<DocumentOptions>();

  mainDocumentSig = computed(() => {
    const documents = this.store.documentsView();
    if (!documents.length) return;
    return (
      // If there are any documents in error state, show the first one
      documents.find((document) => document.state === 'error') ??
      // else show the first document that is still uploading
      documents.find((document) => document.state === 'uploading') ??
      // else show the first document that is done
      documents.find((document) => document.state === 'done')
    );
  });

  constructor() {
    effect(
      () => {
        this.store.loadDocuments(this.optionsSig());
      },
      { allowSignalWrites: true },
    );
  }

  @HostBinding('class') class = 'd-block align-self-start position-relative';

  openDialog(): void {
    this.dialog.open<DialogType, DialogData>(
      SharedPatternDocumentUploadDialogComponent,
      {
        data: { options: this.optionsSig(), store: this.store },
      },
    );
  }
}
