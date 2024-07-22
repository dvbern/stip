import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostBinding,
  Output,
  computed,
  inject,
  input,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { Observable, filter, map, switchMap } from 'rxjs';

import { DocumentView, UploadView } from '@dv/shared/model/dokument';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { FilesizePipe } from '@dv/shared/ui/filesize-pipe';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
  selector: 'dv-shared-pattern-document-upload-entry',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    SharedUiIconChipComponent,
    MatProgressBarModule,
    RouterLink,
    FilesizePipe,
  ],
  templateUrl: './document-upload-entry.component.html',
  styleUrls: ['./document-upload-entry.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternDocumentUploadEntryComponent {
  uploadViewSig = input.required<UploadView>();
  documentViewSig = input.required<DocumentView>();
  loadingSig = input.required<boolean>();
  downloadLinkSig = computed(() => {
    const document = this.documentViewSig();
    const upload = this.uploadViewSig();
    return `/download/${upload.gesuchId}/${upload.type}/${document.file.id}`;
  });
  checkForRemove$ = new EventEmitter<void>();
  @Output() cancelUpload = new EventEmitter<{ dokumentId: string }>();
  @Output() removeUpload: Observable<{ dokumentId: string }>;

  @HostBinding('class') class = 'd-flex flex-row mt-3';

  private dialog = inject(MatDialog);

  constructor() {
    this.removeUpload = this.checkForRemove$.pipe(
      switchMap(() =>
        SharedUiConfirmDialogComponent.open(this.dialog, {
          title: 'shared.file.delete.title',
          message: 'shared.file.delete.message',
          confirmText: 'shared.ui.yes',
          cancelText: 'shared.ui.no',
        }).afterClosed(),
      ),
      filter((confirmed) => !!confirmed),
      map(() => ({ dokumentId: this.documentViewSig().file.id })),
    );
  }
}
