import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

@Component({
  selector: 'dv-shared-ui-form-save',
  standalone: true,
  imports: [CommonModule, SharedUiLoadingComponent, TranslateModule],
  templateUrl: './shared-ui-form-save.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFormSaveComponent {
  @Input() labelKey = 'shared.form.save';
  @Input() loading = false;

  @HostBinding('class') class = 'd-flex gap-3 flex-row align-items-start';
}
