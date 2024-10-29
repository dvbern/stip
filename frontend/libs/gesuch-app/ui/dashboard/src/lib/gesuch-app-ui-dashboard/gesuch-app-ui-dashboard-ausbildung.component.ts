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

import { SharedModelGsAusbildungView } from '@dv/shared/model/ausbildung';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiTranslatedDatePipe } from '@dv/shared/ui/translated-date-pipe';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';

import { GesuchAppUiDashboardGesuchComponent } from './gesuch-app-ui-dashboard-gesuch.component';

@Component({
  selector: 'dv-gesuch-app-ui-dashboard-ausbildung',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule,
    TranslatedPropertyPipe,
    GesuchAppUiDashboardGesuchComponent,
    SharedUiIconChipComponent,
    SharedUiTranslatedDatePipe,
  ],
  templateUrl: './gesuch-app-ui-dashboard-ausbildung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppUiDashboardAusbildungComponent {
  ausbildungSig = input.required<SharedModelGsAusbildungView>();
  deleteGesuch = output<string>();

  @HostBinding('class') defaultClasses =
    'tw-block tw-bg-white tw-p-6 tw-rounded-lg';
}
