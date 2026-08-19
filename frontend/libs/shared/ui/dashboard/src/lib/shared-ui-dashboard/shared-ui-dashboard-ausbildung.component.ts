import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
  output,
} from '@angular/core';
import { RouterLink } from '@angular/router';

import {
  GsDashboardActions,
  SharedModelGsAusbildungView,
} from '@dv/shared/model/ausbildung';
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
  output = output<GsDashboardActions>();

  @HostBinding('class') defaultClasses =
    'tw:block tw:bg-white tw:dv-container tw:rounded-lg';
}
