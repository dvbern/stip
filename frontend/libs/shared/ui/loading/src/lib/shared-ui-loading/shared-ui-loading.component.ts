import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'dv-shared-ui-loading',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './shared-ui-loading.component.html',
  styleUrl: './shared-ui-loading.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiLoadingComponent {}
