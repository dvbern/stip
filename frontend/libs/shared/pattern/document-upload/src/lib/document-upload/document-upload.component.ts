import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnInit,
  computed,
  inject,
  input,
  output,
} from '@angular/core';
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslatePipe } from '@ngx-translate/core';
import { filter } from 'rxjs';

import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  DokumentOptions,
  SharedModelGesuchDokument,
} from '@dv/shared/model/dokument';
import { Dokument } from '@dv/shared/model/gesuch';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

import { SharedPatternDocumentUploadDialogComponent } from '../document-upload-dialog/document-upload-dialog.component';
import { UploadStore } from '../upload.store';

type DialogType = SharedPatternDocumentUploadDialogComponent;
type DialogData = SharedPatternDocumentUploadDialogComponent['data'];

@Component({
  standalone: true,
  selector: 'dv-shared-pattern-document-upload',
  imports: [
    CommonModule,
    TranslatePipe,
    MatDialogModule,
    MatFormFieldModule,
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
  documentsChanged = output();

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

  @HostBinding('class') klass = 'tw-block tw-self-start tw-relative tw-h-14';

  constructor() {
    // Load the gesuch step validity after the state of uploaded documents changes
    toObservable(this.uploadStore.documentChangedSig)
      .pipe(
        filter((x) => x.hasChanged),
        takeUntilDestroyed(),
      )
      .subscribe(() => {
        const { initialDokumente, dokument } = this.optionsSig();

        this.handleDokumentChange(initialDokumente, dokument);
      });
  }

  ngOnInit() {
    const initialDocuments = this.optionsSig()?.initialDokumente;

    if (initialDocuments) {
      this.uploadStore.setInitialDocuments(initialDocuments);
    } else {
      // Only load the documents with the initial required options, not on every change with for example an effect
      this.uploadStore.loadDocuments(this.optionsSig());
    }
  }

  openDialog(): void {
    this.dialog.open<DialogType, DialogData>(
      SharedPatternDocumentUploadDialogComponent,
      {
        data: {
          options: this.optionsSig(),
          dokumentModel: this.gesuchDokumentSig() ?? this.optionsSig().dokument,
          store: this.uploadStore,
        },
      },
    );
  }

  private handleDokumentChange(
    initialDokumente: Dokument[] | undefined,
    dokument: SharedModelGesuchDokument,
  ): void {
    this.einreichStore.validateSteps$({
      gesuchTrancheId: dokument.trancheId,
    });

    if (initialDokumente) {
      this.dokumentsStore.getDokumenteAndRequired$({
        gesuchTrancheId: dokument.trancheId,
      });
    }

    switch (dokument.art) {
      case 'GESUCH_DOKUMENT':
        this.dokumentsStore.getRequiredGesuchDokument$({
          trancheId: dokument.trancheId,
          dokumentTyp: dokument.dokumentTyp,
        });
        break;

      case 'CUSTOM_DOKUMENT':
        break;

      case 'UNTERSCHRIFTENBLATT':
        this.dokumentsStore.getAdditionalDokumente$({
          gesuchId: dokument.gesuchId,
        });
        break;
    }
  }
}
