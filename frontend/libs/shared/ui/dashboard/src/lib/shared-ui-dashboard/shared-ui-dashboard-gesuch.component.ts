import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  computed,
  input,
  output,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedModelGsGesuchView } from '@dv/shared/model/ausbildung';
import { SharedUiAenderungsEntryComponent } from '@dv/shared/ui/aenderungs-entry';

@Component({
  selector: 'dv-shared-ui-dashboard-gesuch',
  imports: [
    CommonModule,
    RouterLink,
    TranslocoPipe,
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
    'tw:flex tw:w-full tw:flex-col tw:rounded-lg tw:px-6 tw:pt-4 tw:pb-6';

  // Purpose: Only show create darlehen button on the Gesuch that belongs to the current year
  isCurrentYearTechnischesJahrSig = computed(() => {
    const gesuch = this.gesuchSig();
    const gesuchsPeriodeStartYear =
      gesuch.gesuchsperiode.gesuchsjahr.technischesJahr;
    const currentYear = new Date().getFullYear();

    return gesuchsPeriodeStartYear === currentYear;
  });
}
