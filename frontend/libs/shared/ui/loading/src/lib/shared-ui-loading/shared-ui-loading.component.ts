import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'dv-shared-ui-loading',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './shared-ui-loading.component.html',
  styleUrl: './shared-ui-loading.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiLoadingComponent {
  @Input() compact = false;
  @Input() loadingTextKey = 'shared.ui.loading.text';

  @HostBinding('class') class = 'd-flex';
}
