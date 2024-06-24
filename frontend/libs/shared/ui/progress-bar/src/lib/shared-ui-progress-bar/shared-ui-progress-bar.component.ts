import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';

import { SharedModelGesuchFormStepProgress } from '@dv/shared/model/gesuch-form';

@Component({
  selector: 'dv-shared-ui-progress-bar',
  standalone: true,
  imports: [],
  templateUrl: './shared-ui-progress-bar.component.html',
  styleUrls: ['./shared-ui-progress-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiProgressBarComponent {
  @Input({ required: true }) current?: SharedModelGesuchFormStepProgress;

  @HostBinding('style.--progress') progress = this.current?.percentage;
}
