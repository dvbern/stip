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
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { filter } from 'rxjs';

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchDokumente } from '@dv/shared/event/gesuch-dokumente';
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
export class SharedPatternDocumentUploadComponent implements OnInit {
  private dialog = inject(MatDialog);
  private globalStore = inject(Store);
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
    // Load the gesuch step validity after the state of uploaded documents changes
    toObservable(this.store.documentChangedSig)
      .pipe(
        filter((x) => x.hasChanged),
        takeUntilDestroyed(),
      )
      .subscribe(() => {
        const initialDocuments = this.optionsSig().initialDocuments;

        this.globalStore.dispatch(
          SharedDataAccessGesuchEvents.gesuchValidateSteps({
            id: this.optionsSig().gesuchId,
          }),
        );

        if (initialDocuments) {
          this.globalStore.dispatch(
            SharedEventGesuchDokumente.loadDocuments({
              gesuchId: this.optionsSig().gesuchId,
            }),
          );
        }
      });
  }

  ngOnInit() {
    const initialDocuments = this.optionsSig()?.initialDocuments;

    if (initialDocuments) {
      this.store.setInitialDocuments(initialDocuments);
    } else {
      // Only load the documents with the initial required options, not on every change with for example an effect
      this.store.loadDocuments(this.optionsSig());
    }
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
