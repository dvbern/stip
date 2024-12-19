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
import { TranslateDirective, TranslatePipe } from '@ngx-translate/core';
import { Observable, filter, map, switchMap } from 'rxjs';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  DokumentView,
  UploadView,
  getDownloadLink,
  isUploadable,
} from '@dv/shared/model/dokument';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { FilesizePipe } from '@dv/shared/ui/filesize-pipe';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
  selector: 'dv-shared-pattern-document-upload-entry',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    TranslateDirective,
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
  documentViewSig = input.required<DokumentView>();
  loadingSig = input.required<boolean>();

  private config = inject(SharedModelCompileTimeConfig);
  downloadLinkSig = computed(() => {
    const dokument = this.documentViewSig();
    return getDownloadLink(dokument);
  });
  isUploadableSig = computed(() => {
    const { dokumentModel, permissions } = this.uploadViewSig();
    return isUploadable(this.config.appType, dokumentModel, permissions);
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
