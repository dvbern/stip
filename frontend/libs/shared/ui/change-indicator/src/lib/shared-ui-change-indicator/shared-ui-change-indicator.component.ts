import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';

@Component({
  selector: 'dv-shared-ui-change-indicator',
  imports: [],
  template: '',
  styleUrl: './shared-ui-change-indicator.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiChangeIndicatorComponent {
  @HostBinding('class') @Input() displaced: 'left' | null = null;
}
