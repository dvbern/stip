import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

@Component({
  selector: 'dv-shared-ui-form-save',
  imports: [SharedUiLoadingComponent, TranslocoPipe],
  templateUrl: './shared-ui-form-save.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFormSaveComponent {
  @Input() labelKey = 'shared.form.save';
  @Input() loading = false;
  @Input() disabled = false;
  @Input() prefixIcon = false;

  @HostBinding('class') class = 'tw-flex';
}
