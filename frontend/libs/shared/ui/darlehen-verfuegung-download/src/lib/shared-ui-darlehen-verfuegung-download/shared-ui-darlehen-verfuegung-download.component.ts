import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';

import { FreiwilligDarlehen } from '@dv/shared/model/gesuch';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';

@Component({
  selector: 'dv-shared-ui-darlehen-verfuegung-download',
  imports: [
    SharedUiAdvTranslocoDirective,
    MatTooltipModule,
    SharedUiDownloadButtonDirective,
  ],
  templateUrl: './shared-ui-darlehen-verfuegung-download.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiDarlehenVerfuegungDownloadComponent {
  darlehenSig = input.required<FreiwilligDarlehen | undefined>();
}
