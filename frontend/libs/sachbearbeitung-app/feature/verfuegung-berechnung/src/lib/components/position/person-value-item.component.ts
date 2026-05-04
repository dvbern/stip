import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
} from '@angular/core';

import { PersonValueItem } from '@dv/shared/model/gesuch';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

@Component({
  selector: 'dv-person-value-item',
  imports: [SharedUiFormatChfPipe],
  template: `
    @for (i of itemsSig(); track $index) {
      <div class="tw:flex tw:justify-between tw:text-gray-500 tw:text-sm">
        <div>{{ i.vorname }}</div>
        <div>{{ i.value | formatChf }}</div>
      </div>
    }
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersonValueItemComponent {
  @HostBinding('class')
  readonly klass = 'tw:block';
  itemsSig = input.required<PersonValueItem[]>();
}
