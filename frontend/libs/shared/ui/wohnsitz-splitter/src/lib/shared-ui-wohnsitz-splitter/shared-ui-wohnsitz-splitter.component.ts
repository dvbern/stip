import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { FormControl } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiPercentageSplitterComponent } from '@dv/shared/ui/percentage-splitter';

@Component({
  selector: 'dv-shared-ui-wohnsitz-splitter',
  standalone: true,
  imports: [SharedUiPercentageSplitterComponent, TranslateModule],
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
}
