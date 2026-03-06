import { Component, HostBinding } from '@angular/core';

@Component({
  selector: 'dv-shared-pattern-gesuch-info-bar',
  imports: [],
  templateUrl: './shared-pattern-gesuch-info-bar.component.html',
})
export class SharedPatternGesuchInfoBarComponent {
  @HostBinding('class') class = 'tw:bg-white tw:block tw:my-6';
}
