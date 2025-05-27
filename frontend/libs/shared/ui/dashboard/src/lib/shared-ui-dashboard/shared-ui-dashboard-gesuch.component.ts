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

import { SharedModelGsGesuchView } from '@dv/shared/model/ausbildung';
import { SharedUiAenderungsEntryComponent } from '@dv/shared/ui/aenderungs-entry';

@Component({
  selector: 'dv-shared-ui-dashboard-gesuch',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    TranslatePipe,
    SharedUiAenderungsEntryComponent,
  ],
  templateUrl: './shared-ui-dashboard-gesuch.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiDashboardGesuchComponent {
  gesuchSig = input.required<SharedModelGsGesuchView>();
  deleteGesuch = output<string>();
  deleteAenderung = output<string>();
  aenderungMelden = output<void>();

  @HostBinding('class') defaultClasses =
    'tw-flex tw-w-full tw-flex-col tw-rounded-lg tw-px-6 tw-pt-4 tw-pb-6';
}
