import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

@Component({
  selector: 'dv-shared-ui-comming-soon',
  imports: [TranslocoPipe],
  templateUrl: './shared-ui-comming-soon.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiCommingSoonComponent {}
