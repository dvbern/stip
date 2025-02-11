import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  input,
  output,
} from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

import { SharedModelTableDokument } from '@dv/shared/model/dokument';
import { SharedUiIconBadgeComponent } from '@dv/shared/ui/icon-badge';
import { SharedUiPrefixAppTypePipe } from '@dv/shared/ui/prefix-app-type';

@Component({
  selector: 'dv-dokument-status-actions',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    SharedUiIconBadgeComponent,
    SharedUiPrefixAppTypePipe,
  ],
  templateUrl: './dokument-status-actions.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DokumentStatusActionsComponent {
  dokumentSig = input.required<SharedModelTableDokument>();
  viewSig = input.required<{
    isSachbearbeitungApp: boolean;
    readonly: boolean;
  }>();

  dokumentAkzeptieren = output<SharedModelTableDokument>();
  dokumentAblehnen = output<SharedModelTableDokument>();
}
