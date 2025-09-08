import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { FormControl } from '@angular/forms';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedUiZuvorHintDirective } from '@dv/shared/ui/form';
import {
  SharedUiPercentageSplitterComponent,
  SharedUiPercentageSplitterDirective,
} from '@dv/shared/ui/percentage-splitter';

@Component({
  selector: 'dv-shared-ui-wohnsitz-splitter',
  imports: [
    SharedUiPercentageSplitterComponent,
    SharedUiPercentageSplitterDirective,
    SharedUiZuvorHintDirective,
    TranslocoPipe,
  ],
  templateUrl: './shared-ui-wohnsitz-splitter.component.html',
  styleUrls: ['./shared-ui-wohnsitz-splitter.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiWohnsitzSplitterComponent {
  @Input({ required: true }) updateValidity: unknown;
  @Input({ required: true }) controls!: {
    wohnsitzAnteilMutter: FormControl<string | undefined>;
    wohnsitzAnteilVater: FormControl<string | undefined>;
  };

  @Input({ required: true }) changes!: Partial<{
    wohnsitzAnteilVater: number;
    wohnsitzAnteilMutter: number;
  }>;
}
