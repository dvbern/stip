import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
  output,
} from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedModelGsAusbildungView } from '@dv/shared/model/ausbildung';
import { AenderungMelden } from '@dv/shared/model/gesuch';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

import { SharedUiDashboardGesuchComponent } from './shared-ui-dashboard-gesuch.component';

@Component({
  selector: 'dv-shared-ui-dashboard-compact-ausbildung',
  imports: [
    CommonModule,
    TranslocoPipe,
    MatExpansionModule,
    SharedUiDashboardGesuchComponent,
    SharedUiIconChipComponent,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './shared-ui-dashboard-compact-ausbildung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiDashboardCompactAusbildungComponent {
  ausbildungSig = input.required<SharedModelGsAusbildungView>();
  deleteGesuch = output<string>();
  deleteAenderung = output<string>();
  aenderungMelden = output<AenderungMelden>();

  @HostBinding('class') defaultClasses = 'tw:block';
}
