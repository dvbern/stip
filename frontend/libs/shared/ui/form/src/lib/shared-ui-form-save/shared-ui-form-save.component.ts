import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

@Component({
  selector: 'dv-shared-ui-form-save',
  imports: [CommonModule, SharedUiLoadingComponent, TranslatePipe],
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
