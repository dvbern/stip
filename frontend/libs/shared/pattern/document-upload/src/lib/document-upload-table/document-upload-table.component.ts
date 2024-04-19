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
import { distinctUntilChanged, skip } from 'rxjs';

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { SharedUiDropFileComponent } from '@dv/shared/ui/drop-file';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

import { SharedPatternDocumentUploadDialogComponent } from '../document-upload-dialog/document-upload-dialog.component';
import { DocumentOptions } from '../upload.model';
import { UploadStore } from '../upload.store';

type DialogType = SharedPatternDocumentUploadDialogComponent;
type DialogData = SharedPatternDocumentUploadDialogComponent['data'];

@Component({
  selector: 'dv-shared-pattern-document-upload-table',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatDialogModule,
    MatFormFieldModule,
    SharedUiDropFileComponent,
    SharedUiIconChipComponent,
  ],
  templateUrl: './document-upload-table.component.html',
  styleUrl: './document-upload-table.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternDocumentUploadTableComponent implements OnInit {
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
    toObservable(this.store.hasUploadedEntriesSig)
      .pipe(skip(1), distinctUntilChanged(), takeUntilDestroyed())
      .subscribe(() => {
        this.globalStore.dispatch(
          SharedDataAccessGesuchEvents.gesuchValidateSteps({
            id: this.optionsSig().gesuchId,
          }),
        );
      });
  }

  ngOnInit() {
    // Only load the documents with the initial required options, not on every change with for example an effect
    this.store.loadDocuments(this.optionsSig());
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
