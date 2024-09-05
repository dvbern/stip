import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'dv-shared-ui-change-indicator',
  standalone: true,
  imports: [CommonModule],
  template: '',
  styleUrl: './shared-ui-change-indicator.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiChangeIndicatorComponent {}
