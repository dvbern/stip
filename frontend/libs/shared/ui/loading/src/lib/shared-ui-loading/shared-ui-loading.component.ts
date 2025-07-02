import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  standalone: true,
  selector: 'dv-shared-ui-loading',
  imports: [CommonModule, TranslatePipe],
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
