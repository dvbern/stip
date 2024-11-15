import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';

import { fromBackendLocalDate } from '@dv/shared/util/validator-date';

@Component({
  selector: 'dv-shared-ui-tooltip-date',
  standalone: true,
  imports: [CommonModule, MatTooltipModule],
  templateUrl: './shared-ui-tooltip-date.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiTooltipDateComponent {
  dateSig = input.required<Date | string | null | undefined>();
  formatSig = input<string>('dd.MM.yyyy');

  dateViewSig = computed(() => {
    const date = this.dateSig();
    if (!date) {
      return null;
    }
    if (typeof date === 'string') {
      return fromBackendLocalDate(date) ?? null;
    }
    return date;
  });
}
