import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  input,
  output,
} from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedModelTableDokument } from '@dv/shared/model/dokument';
import { Gesuchstatus } from '@dv/shared/model/gesuch';
import { SharedUiIconBadgeComponent } from '@dv/shared/ui/icon-badge';
import { SharedUiReplaceAppTypePipe } from '@dv/shared/ui/replace-app-type';

@Component({
  selector: 'dv-dokument-status-actions',
  imports: [
    CommonModule,
    TranslocoPipe,
    SharedUiIconBadgeComponent,
    SharedUiReplaceAppTypePipe,
  ],
  templateUrl: './dokument-status-actions.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DokumentStatusActionsComponent {
  dokumentSig = input.required<SharedModelTableDokument>();
  viewSig = input.required<{
    canApproveDecline: boolean;
    isSachbearbeitungApp: boolean;
    readonly: boolean;
    gesuchStatus?: Gesuchstatus;
  }>();

  dokumentAkzeptieren = output<SharedModelTableDokument>();
  dokumentAblehnen = output<SharedModelTableDokument>();
}
