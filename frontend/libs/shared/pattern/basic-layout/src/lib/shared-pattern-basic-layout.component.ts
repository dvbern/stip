import { ChangeDetectionStrategy, Component, HostBinding } from '@angular/core';

import { SharedPatternGlobalHeaderComponent } from '@dv/shared/pattern/global-header';

@Component({
  selector: 'dv-shared-pattern-basic-layout',
  imports: [SharedPatternGlobalHeaderComponent],
  templateUrl: './shared-pattern-basic-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedPatternBasicLayoutComponent {
  @HostBinding('class') klass = 'tw:flex tw:flex-col';
}
