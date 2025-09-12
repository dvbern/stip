import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

@Component({
  standalone: true,
  selector: 'dv-shared-ui-loading',
  imports: [TranslocoDirective],
  templateUrl: './shared-ui-loading.component.html',
  styleUrl: './shared-ui-loading.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiLoadingComponent {
  @Input() type: 'spinner' | 'compact' | 'icon' = 'spinner';
  @Input() overlay = false;
  @Input() loadingTextKey = 'shared.ui.loading.text';

  @HostBinding('class') class = 'd-flex';
}
