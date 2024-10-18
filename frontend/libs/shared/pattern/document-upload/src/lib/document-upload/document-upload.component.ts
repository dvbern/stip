import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnInit,
  computed,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslateModule } from '@ngx-translate/core';
import { filter } from 'rxjs';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { DokumentOptions } from '@dv/shared/model/dokument';
import { SharedUiDropFileComponent } from '@dv/shared/ui/drop-file';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

import { SharedPatternDocumentUploadDialogComponent } from '../document-upload-dialog/document-upload-dialog.component';
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
export class SharedPatternDocumentUploadComponent implements OnInit {
  private dialog = inject(MatDialog);
  private dokumentsStore = inject(DokumentsStore);
  private uploadStore = inject(UploadStore);
  private einreichStore = inject(EinreichenStore);
  optionsSig = input.required<DokumentOptions>();

  gesuchDokumentSig = computed(() => {
    const { gesuchDokument } = this.uploadStore.dokumentListView();
    return gesuchDokument;
  });

  mainDocumentSig = computed(() => {
    const { dokuments } = this.uploadStore.dokumentListView();
    if (!dokuments.length) return;
    return (
      // If there are any documents in error state, show the first one
      dokuments.find((document) => document.state === 'error') ??
      // else show the first document that is still uploading
      dokuments.find((document) => document.state === 'uploading') ??
      // else show the first document that is done
      dokuments.find((document) => document.state === 'done')
    );
  });

  constructor() {
    // Load the gesuch step validity after the state of uploaded documents changes
    toObservable(this.uploadStore.documentChangedSig)
      .pipe(
        filter((x) => x.hasChanged),
        takeUntilDestroyed(),
      )
      .subscribe(() => {
        const initialDocuments = this.optionsSig().initialDocuments;

        this.einreichStore.validateSteps$({
          gesuchTrancheId: this.optionsSig().trancheId,
        });

        if (initialDocuments) {
          this.dokumentsStore.getDokumenteAndRequired$(
            this.optionsSig().trancheId,
          );
        }
      });
  }

  ngOnInit() {
    const initialDocuments = this.optionsSig()?.initialDocuments;

    if (initialDocuments) {
      this.uploadStore.setInitialDocuments(initialDocuments);
    } else {
      // Only load the documents with the initial required options, not on every change with for example an effect
      this.uploadStore.loadDocuments(this.optionsSig());
    }
  }

  @HostBinding('class') class = 'd-block align-self-start position-relative';

  openDialog(): void {
    this.dialog.open<DialogType, DialogData>(
      SharedPatternDocumentUploadDialogComponent,
      {
        data: {
          options: this.optionsSig(),
          gesuchDokument:
            this.gesuchDokumentSig() ?? this.optionsSig().gesuchDokument,
          store: this.uploadStore,
        },
      },
    );
  }
}
