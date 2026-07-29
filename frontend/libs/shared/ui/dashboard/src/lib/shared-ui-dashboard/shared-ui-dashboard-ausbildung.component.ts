import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
  output,
} from '@angular/core';
import { RouterLink } from '@angular/router';

import { SharedModelGsAusbildungView } from '@dv/shared/model/ausbildung';
import { AenderungMelden } from '@dv/shared/model/gesuch';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

import { SharedUiDashboardGesuchSubitemContent } from './shared-ui-dashboard-gesuch-subitem-content.directive';
import { SharedUiDashboardGesuchSubItemComponent } from './shared-ui-dashboard-gesuch-subitem.component';
import { SharedUiDashboardGesuchComponent } from './shared-ui-dashboard-gesuch.component';

@Component({
  selector: 'dv-shared-ui-dashboard-ausbildung',
  imports: [
    CommonModule,
    RouterLink,
    SharedUiDashboardGesuchComponent,
    SharedUiDashboardGesuchSubItemComponent,
    SharedUiDashboardGesuchSubitemContent,
    SharedUiIconChipComponent,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './shared-ui-dashboard-ausbildung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiDashboardAusbildungComponent {
  ausbildungSig = input.required<SharedModelGsAusbildungView>();
  deleteAusbildung = output<SharedModelGsAusbildungView>();
  ausbildungUnterbrechen = output<string>();
  deleteGesuch = output<string>();
  deleteAenderung = output<string>();
  aenderungMelden = output<AenderungMelden>();

  @HostBinding('class') defaultClasses =
    'tw:block tw:bg-white tw:py-8 tw:px-6 tw:rounded-lg';
}
