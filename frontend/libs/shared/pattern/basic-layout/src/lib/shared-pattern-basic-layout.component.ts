import { ChangeDetectionStrategy, Component, HostBinding } from '@angular/core';

import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';

// todo: remove on demo-data app as well?

@Component({
  selector: 'dv-shared-pattern-basic-layout',
  imports: [SharedPatternAppHeaderComponent],
  templateUrl: './shared-pattern-basic-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternBasicLayoutComponent {
  @HostBinding('class') klass = 'tw:flex tw:flex-col';
}
