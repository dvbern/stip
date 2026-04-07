import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';

import { DemoDataAppUiAdvTranslocoDirective } from '@dv/demo-data-app/ui/adv-transloco-directive';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

type ValidValue = string | boolean | number | null | undefined;

@Component({
  templateUrl: './comparison-value.component.html',
  selector: 'dv-comparison-value',
  host: {
    class: 'tw:inline-flex',
  },
  imports: [
    CommonModule,
    MatTooltipModule,
    DemoDataAppUiAdvTranslocoDirective,
    SharedUiFormatChfPipe,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ComparisonValueComponent {
  valueSig = input.required({
    transform: (value: ValidValue) => typed(value),
  });
}

const typed = (value: unknown) => {
  switch (typeof value) {
    case 'bigint':
    case 'number': {
      return { value: value as number, type: 'number' as const };
    }
    case 'boolean': {
      return { value, type: 'boolean' as const };
    }
    case 'string': {
      const intValue = parseInt(value);
      if (isNaN(intValue)) {
        return { value, type: 'string' as const };
      }
      return { value: intValue, type: 'number' as const };
    }
    default: {
      return { value: undefined, type: 'unwanted' as const };
    }
  }
};
