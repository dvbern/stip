import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
  output,
} from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedModelGsAusbildungView } from '@dv/shared/model/ausbildung';
import { AenderungMelden } from '@dv/shared/model/gesuch';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiTranslatedDatePipe } from '@dv/shared/ui/translated-date-pipe';

import { SharedUiDashboardGesuchComponent } from './shared-ui-dashboard-gesuch.component';

@Component({
  selector: 'dv-shared-ui-dashboard-ausbildung',
  imports: [
    CommonModule,
    TranslocoPipe,
    SharedUiDashboardGesuchComponent,
    SharedUiIconChipComponent,
    SharedUiTranslatedDatePipe,
  ],
  templateUrl: './shared-ui-dashboard-ausbildung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiDashboardAusbildungComponent {
  ausbildungSig = input.required<SharedModelGsAusbildungView>();
  deleteAusbildung = output<SharedModelGsAusbildungView>();
  deleteGesuch = output<string>();
  deleteAenderung = output<string>();
  aenderungMelden = output<AenderungMelden>();

  @HostBinding('class') defaultClasses =
    'tw-block tw-bg-white tw-p-6 tw-rounded-lg';
}
