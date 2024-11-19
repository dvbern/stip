import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
  output,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { GesuchAppUiAenderungsEntryComponent } from '@dv/gesuch-app/ui/aenderungs-entry';
import { SharedModelGsGesuchView } from '@dv/shared/model/ausbildung';

@Component({
  selector: 'dv-gesuch-app-ui-dashboard-gesuch',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslatePipe,
    GesuchAppUiAenderungsEntryComponent,
  ],
  templateUrl: './gesuch-app-ui-dashboard-gesuch.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppUiDashboardGesuchComponent {
  gesuchSig = input.required<SharedModelGsGesuchView>();
  deleteGesuch = output<string>();
  deleteAenderung = output<string>();
  aenderungMelden = output<void>();

  @HostBinding('class') defaultClasses =
    'tw-flex tw-w-full tw-flex-col tw-rounded-lg tw-px-6 tw-pt-4 tw-pb-6';
}
