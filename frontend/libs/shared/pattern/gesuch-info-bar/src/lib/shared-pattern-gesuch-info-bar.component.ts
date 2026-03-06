import { Component, HostBinding } from '@angular/core';

@Component({
  selector: 'dv-shared-pattern-gesuch-info-bar',
  template: `
    <div
      class="tw:flex tw:items-center tw:justify-between tw: tw:w-full tw:shadow-md tw:shadow-gray-200 tw:border tw:border-gray-300 tw:rounded-xl tw:p-4"
    >
      <ng-content select="[dvGesuchStatusIndication]"></ng-content>
      <ng-content select="[dvGesuchNavItems]"></ng-content>
    </div>
  `,
})
export class SharedPatternGesuchInfoBarComponent {
  @HostBinding('class') class = 'tw:bg-white tw:block tw:my-6';
}
