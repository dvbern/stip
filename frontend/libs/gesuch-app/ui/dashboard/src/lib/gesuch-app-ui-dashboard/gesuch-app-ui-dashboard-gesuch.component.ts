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

import { SharedModelGsGesuchView } from '@dv/shared/model/ausbildung';
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
    SharedUiIconChipComponent,
    TranslatedPropertyPipe,
    SharedUiTranslatedDatePipe,
  ],
  templateUrl: './gesuch-app-ui-dashboard-gesuch.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppUiDashboardGesuchComponent {
  gesuchSig = input.required<SharedModelGsGesuchView>();
  deleteGesuch = output<string>();

  @HostBinding('class') defaultClasses =
    'tw-flex tw-w-full tw-flex-col tw-rounded-lg tw-px-6 tw-pt-4 tw-pb-6';
}
