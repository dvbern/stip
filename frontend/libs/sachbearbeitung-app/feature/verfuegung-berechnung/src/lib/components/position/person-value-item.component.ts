import { ChangeDetectionStrategy, Component, input } from '@angular/core';

import { PersonValueItem } from '@dv/shared/model/gesuch';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

@Component({
  selector: 'dv-person-value-item',
  imports: [SharedUiFormatChfPipe],
  template: `
    <div class="tw:flex tw:gap-1">
      @for (i of itemsSig(); track $index) {
        <div class="tw:text-gray-500 tw:text-sm">
          <span>{{ i.vorname }}: </span>
          <span>{{ i.value | formatChf }}</span>
          @if (!$last) {
            <span>,</span>
          }
        </div>
      }
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersonValueItemComponent {
  itemsSig = input.required<PersonValueItem[]>();
}
