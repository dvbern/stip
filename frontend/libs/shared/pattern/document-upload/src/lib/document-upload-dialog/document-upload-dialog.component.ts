import { DialogRef } from '@angular/cdk/dialog';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { Subject, mergeMap } from 'rxjs';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  DokumentOptions,
  SharedModelGesuchDokument,
  UploadView,
  isUploadable,
} from '@dv/shared/model/dokument';
import { SharedUiDropFileComponent } from '@dv/shared/ui/drop-file';
import { SharedUtilDocumentMergerService } from '@dv/shared/util/document-merger';

import { DocumentUploadApprovalComponent } from '../document-upload-approval/document-upload-approval.component';
import { SharedPatternDocumentUploadListComponent } from '../document-upload-list/document-upload-list.component';
import { UploadStore } from '../upload.store';

@Component({
    selector: 'dv-shared-pattern-document-upload-dialog',
    imports: [
        TranslatePipe,
        MatDialogModule,
        MatFormFieldModule,
        SharedUiDropFileComponent,
        SharedPatternDocumentUploadListComponent,
        DocumentUploadApprovalComponent,
    ],
    templateUrl: './document-upload-dialog.component.html',
    styleUrls: ['./document-upload-dialog.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SharedPatternDocumentUploadDialogComponent {
  data = inject<{
    options: DokumentOptions;
    dokumentModel: SharedModelGesuchDokument;
    store: UploadStore;
  }>(MAT_DIALOG_DATA);
  private translate = inject(TranslateService);
  private dialogRef = inject(DialogRef);
  private documentMerger = inject(SharedUtilDocumentMergerService);
  private config = inject(SharedModelCompileTimeConfig);

  uploadViewSig = computed<UploadView>(() => ({
    type: this.data.options.dokument.art,
    permissions: this.data.options.permissions,
    dokumentModel: this.data.dokumentModel,
    initialDokuments: this.data.options.initialDokumente,
    hasEntries: this.data.store.hasEntriesSig(),
    isSachbearbeitungApp: this.config.isSachbearbeitungApp,
  }));

  showUplaodSig = computed(() => {
    const { options } = this.data;

    return isUploadable(options.dokument, options.permissions);
  });

  private newDocuments$ = new Subject<File[]>();

  constructor() {
    const { options, store } = this.data;
    this.newDocuments$
      .pipe(
        mergeMap((files) =>
          this.documentMerger.mergeImageDocuments(
            this.translate.instant(options.titleKey),
            files,
          ),
        ),
      )
      .subscribe((documents) => {
        documents.forEach((document) => {
          store.uploadDocument(document, options);
        });
      });
  }

  handleMultipleDocumentsAdded(documents: File[]) {
    this.newDocuments$.next(documents);
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
