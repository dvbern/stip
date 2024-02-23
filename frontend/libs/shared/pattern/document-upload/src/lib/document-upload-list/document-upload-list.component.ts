import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  input,
} from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslateModule } from '@ngx-translate/core';

import { SharedPatternDocumentUploadEntryComponent } from './document-upload-entry.component';
import { UploadStore } from '../upload.store';

@Component({
  selector: 'dv-shared-pattern-document-upload-list',
  standalone: true,
  imports: [
    TranslateModule,
    MatFormFieldModule,
    SharedPatternDocumentUploadEntryComponent,
  ],
  templateUrl: './document-upload-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternDocumentUploadListComponent {
  storeSig = input.required<UploadStore>();
  hasEntriesSig = input.required<boolean>();

  @Output() cancelUpload = new EventEmitter<{ dokumentId: string }>();
  @Output() removeUpload = new EventEmitter<{ dokumentId: string }>();
}
