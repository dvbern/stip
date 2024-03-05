import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  HostBinding,
  Output,
  input,
} from '@angular/core';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { TranslateModule } from '@ngx-translate/core';

import { FilesizePipe } from '@dv/shared/ui/filesize-pipe';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

import { DocumentView } from '../upload.model';

@Component({
  selector: 'dv-shared-pattern-document-upload-entry',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    SharedUiIconChipComponent,
    MatProgressBarModule,
    FilesizePipe,
  ],
  templateUrl: './document-upload-entry.component.html',
  styleUrls: ['./document-upload-entry.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternDocumentUploadEntryComponent {
  loadingSig = input.required<boolean>();
  documentSig = input.required<DocumentView>();
  @Output() cancelUpload = new EventEmitter<{ dokumentId: string }>();
  @Output() removeUpload = new EventEmitter<{ dokumentId: string }>();

  @HostBinding('class') class = 'd-flex flex-row mt-3';
}
