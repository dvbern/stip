import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  input,
} from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslatePipe } from '@ngx-translate/core';

import { UploadView } from '@dv/shared/model/dokument';

import { SharedPatternDocumentUploadEntryComponent } from './document-upload-entry.component';
import { UploadStore } from '../upload.store';

@Component({
  selector: 'dv-shared-pattern-document-upload-list',
  standalone: true,
  imports: [
    TranslatePipe,
    MatFormFieldModule,
    SharedPatternDocumentUploadEntryComponent,
  ],
  templateUrl: './document-upload-list.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternDocumentUploadListComponent {
  uploadViewSig = input.required<UploadView>();
  storeSig = input.required<UploadStore>();

  @Output() cancelUpload = new EventEmitter<{ dokumentId: string }>();
  @Output() removeUpload = new EventEmitter<{ dokumentId: string }>();
}
