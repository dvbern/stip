import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
  output,
} from '@angular/core';
import { RouterLink } from '@angular/router';

import { SharedModelGsGesuchView } from '@dv/shared/model/ausbildung';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';

@Component({
  selector: 'dv-shared-ui-dashboard-gesuch',
  imports: [CommonModule, RouterLink, SharedUiAdvTranslocoDirective],
  templateUrl: './shared-ui-dashboard-gesuch.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiDashboardGesuchComponent {
  gesuchSig = input.required<SharedModelGsGesuchView>();
  deleteGesuch = output<string>();
  deleteAenderung = output<string>();
  aenderungMelden = output<void>();
  @HostBinding('class') defaultClasses =
    'tw:flex tw:w-full tw:flex-col tw:rounded-lg tw:p-6';
}
