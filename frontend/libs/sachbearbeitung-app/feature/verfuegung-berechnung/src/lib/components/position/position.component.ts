import { Component, input } from '@angular/core';

import { PersonValueItem } from '@dv/shared/model/gesuch';

import { PersonValueItemComponent } from './person-value-item.component';

@Component({
  selector: 'dv-position',
  imports: [PersonValueItemComponent],
  template: `<div class="tw:flex tw:gap-2">
    <div class="tw:flex-1">
      {{ titleSig() }}

      <!-- todo: improve projection handling -->
      @if (infoSig()) {
        <div class="tw:text-gray-500 tw:text-sm">
          <div>
            {{ infoSig() }}
          </div>
        </div>
      } @else {
        <ng-content></ng-content>
      }
      @if (personValueItemsSig()?.length) {
        <dv-person-value-item
          [itemsSig]="personValueItemsSig() ?? []"
        ></dv-person-value-item>
      }
    </div>
    <div class="tw:text-gray-500 tw:whitespace-nowrap">
      {{ amountSig() }}
    </div>
  </div>`,
})
export class PositionComponent {
  personValueItemsSig = input<PersonValueItem[]>();
  titleSig = input.required<string>();
  /**
   * Optional info text displayed below the title
   * If not provided, the content projection is used,
   * but not both!
   */
  infoSig = input<string>();
  /**
   * Total value as string, formatted with currency pipe
   */
  amountSig = input<string>();
}
