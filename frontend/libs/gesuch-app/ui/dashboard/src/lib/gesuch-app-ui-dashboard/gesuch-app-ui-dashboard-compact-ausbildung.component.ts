import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
  output,
} from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { SharedModelGsAusbildungView } from '@dv/shared/model/ausbildung';
import { AenderungMelden } from '@dv/shared/model/gesuch';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiTranslatedDatePipe } from '@dv/shared/ui/translated-date-pipe';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';

import { GesuchAppUiDashboardGesuchComponent } from './gesuch-app-ui-dashboard-gesuch.component';

@Component({
  selector: 'dv-gesuch-app-ui-dashboard-compact-ausbildung',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslateModule,
    TranslatedPropertyPipe,
    MatExpansionModule,
    GesuchAppUiDashboardGesuchComponent,
    SharedUiIconChipComponent,
    SharedUiTranslatedDatePipe,
  ],
  templateUrl: './gesuch-app-ui-dashboard-compact-ausbildung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppUiDashboardCompactAusbildungComponent {
  ausbildungSig = input.required<SharedModelGsAusbildungView>();
  deleteGesuch = output<string>();
  deleteAenderung = output<string>();
  aenderungMelden = output<AenderungMelden>();

  @HostBinding('class') defaultClasses = 'tw-block';
}
