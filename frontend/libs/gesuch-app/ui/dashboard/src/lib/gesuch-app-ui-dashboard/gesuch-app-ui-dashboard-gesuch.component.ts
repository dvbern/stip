import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
  output,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { GesuchAppUiAenderungsEntryComponent } from '@dv/gesuch-app/ui/aenderungs-entry';
import { SharedModelGsGesuchView } from '@dv/shared/model/ausbildung';
import { AusbildungsStatus } from '@dv/shared/model/gesuch';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiTranslatedDatePipe } from '@dv/shared/ui/translated-date-pipe';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';

@Component({
  selector: 'dv-gesuch-app-ui-dashboard-gesuch',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule,
    TranslatedPropertyPipe,
    GesuchAppUiAenderungsEntryComponent,
    SharedUiIconChipComponent,
    SharedUiTranslatedDatePipe,
  ],
  templateUrl: './gesuch-app-ui-dashboard-gesuch.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppUiDashboardGesuchComponent {
  ausbildungsStatus = input.required<AusbildungsStatus>();
  gesuchSig = input.required<SharedModelGsGesuchView>();
  deleteGesuch = output<string>();
  deleteAenderung = output<string>();
  aenderungMelden = output<void>();

  @HostBinding('class') defaultClasses =
    'tw-flex tw-w-full tw-flex-col tw-rounded-lg tw-px-6 tw-pt-4 tw-pb-6';
}
