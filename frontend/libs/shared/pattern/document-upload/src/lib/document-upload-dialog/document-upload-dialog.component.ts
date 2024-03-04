import { DialogRef } from '@angular/cdk/dialog';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  inject,
} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslateModule } from '@ngx-translate/core';
import { Subject } from 'rxjs';

import { SharedUiDropFileComponent } from '@dv/shared/ui/drop-file';

import { SharedPatternDocumentUploadListComponent } from '../document-upload-list/document-upload-list.component';
import { DocumentOptions } from '../upload.model';
import { UploadStore } from '../upload.store';

@Component({
  selector: 'dv-shared-pattern-document-upload-dialog',
  standalone: true,
  imports: [
    TranslateModule,
    MatDialogModule,
    MatFormFieldModule,
    SharedUiDropFileComponent,
    SharedPatternDocumentUploadListComponent,
  ],
  templateUrl: './document-upload-dialog.component.html',
  styleUrls: ['./document-upload-dialog.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternDocumentUploadDialogComponent {
  data = inject<{ options: DocumentOptions; store: UploadStore }>(
    MAT_DIALOG_DATA,
  );
  dialogRef = inject(DialogRef);

  showUplaodSig = computed(() => {
    const { options, store } = this.data;
    if (options.multiple) {
      return true;
    }
    return !store.hasEntriesSig() || store.isLoading();
  });

  @HostBinding('class') class = 'p-4 p-md-5';

  private newDocuments$ = new Subject<File[]>();

  constructor() {
    const { options, store } = this.data;
    this.newDocuments$.subscribe((documents) => {
      documents.forEach((document) => {
        store.uploadDocument(document, options);
      });
    });
  }

  handleMultipleDocumentsAdded(documents: File[]) {
    const files = this.data.options.multiple
      ? documents
      : documents.slice(0, 1);
    this.newDocuments$.next(files);
  }

  handleFilInputEvent(target: EventTarget | null) {
    if (target && isHTMLInputElement(target) && target.files) {
      this.newDocuments$.next(Array.from(target.files));
      target.value = '';
    }
  }

  closeDialog() {
    this.dialogRef.close();
  }
}

const isHTMLInputElement = (
  target: EventTarget,
): target is HTMLInputElement => {
  return 'files' in target;
};
