import { ChangeDetectionStrategy, Component, HostBinding } from '@angular/core';

import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-basic-layout',
  imports: [SharedPatternAppHeaderComponent],
  templateUrl: './sachbearbeitung-app-pattern-basic-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternBasicLayoutComponent {
  @HostBinding('class') class = 'tw-flex tw-flex-col';
}
