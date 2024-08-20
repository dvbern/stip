import { DialogRef } from '@angular/cdk/dialog';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Subject, mergeMap } from 'rxjs';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { DocumentOptions } from '@dv/shared/model/dokument';
import { SharedUiDropFileComponent } from '@dv/shared/ui/drop-file';
import { SharedUiIfGesuchstellerDirective } from '@dv/shared/ui/if-app-type';
import { SharedUtilDocumentMergerService } from '@dv/shared/util/document-merger';

import { DocumentUploadApprovalComponent } from '../document-upload-approval/document-upload-approval.component';
import { SharedPatternDocumentUploadListComponent } from '../document-upload-list/document-upload-list.component';
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
    SharedUiIfGesuchstellerDirective,
    DocumentUploadApprovalComponent,
  ],
  templateUrl: './document-upload-dialog.component.html',
  styleUrls: ['./document-upload-dialog.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternDocumentUploadDialogComponent {
  data = inject<{ options: DocumentOptions; store: UploadStore }>(
    MAT_DIALOG_DATA,
  );
  translate = inject(TranslateService);
  dialogRef = inject(DialogRef);
  documentMerger = inject(SharedUtilDocumentMergerService);
  config = inject(SharedModelCompileTimeConfig);

  uploadViewSig = computed(() => ({
    gesuchId: this.data.options.gesuchId,
    type: this.data.options.dokumentTyp,
    readonly: this.data.options.readonly,
    isSachbearbeitungApp: this.config.isSachbearbeitungApp,
  }));

  showUplaodSig = computed(() => {
    const { options, store } = this.data;
    if (options.readonly) {
      return false;
    }
    if (!options.singleUpload) {
      return true;
    }
    return !store.hasEntriesSig() || store.isLoading();
  });

  // @HostBinding('class') class = 'p-4 p-md-5';

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
    const files = this.data.options.singleUpload
      ? documents.slice(0, 1)
      : documents;
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
