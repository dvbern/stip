import { CommonModule } from '@angular/common';
import { Component, HostBinding, input } from '@angular/core';

import { PersonValueItem } from '@dv/shared/model/gesuch';

import { PersonValueItemComponent } from './person-value-item.component';

@Component({
  selector: 'dv-position',
  imports: [PersonValueItemComponent, CommonModule],
  template: `<div
      [ngClass]="{ 'tw:pt-4': padding() === 'padding' }"
      class="tw:pb-4"
    >
      <div class="tw:flex tw:gap-2">
        <div class="tw:flex-1">
          <div [ngClass]="{ 'tw:font-bold': type() === 'title' }">
            {{ titleSig() }}
            <ng-content select="title-appendix"></ng-content>
          </div>

          @if (infoSig()) {
            <div class="tw:italic tw:mt-1 tw:text-sm">
              <div>
                {{ infoSig() }}
              </div>
            </div>
          } @else {
            <ng-content></ng-content>
          }
        </div>
        <div
          class="tw:whitespace-nowrap"
          [ngClass]="{
            'tw:font-semibold tw:text-lg': type() === 'title',
          }"
        >
          {{ amountSig() }}
        </div>
      </div>
      @if (personValueItemsSig()?.length) {
        <dv-person-value-item
          class="tw:mt-4"
          [itemsSig]="personValueItemsSig() ?? []"
        ></dv-person-value-item>
      }
    </div>
    @if (sepparator() === 'separator') {
      <div class="tw:border-b tw:border-b-gray-300"></div>
    } `,
})
export class PositionComponent {
  personValueItemsSig = input<PersonValueItem[]>();
  titleSig = input.required<string>();
  @HostBinding('class')
  readonly klass = 'tw:block';
  /**
   * Optional info text displayed below the title
   * If not provided, the content projection is used,
   * but not both!
   */
  infoSig = input<string>();
  /**
   * Total value as string, formatted with currency pipe
   */
  amountSig = input<string | number>();
  type = input<'default' | 'title'>('default');
  padding = input<'padding' | 'no-padding'>('padding');
  sepparator = input<'separator' | 'no-separator'>('separator');
}
