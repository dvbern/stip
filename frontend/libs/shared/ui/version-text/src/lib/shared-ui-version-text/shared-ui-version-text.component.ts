import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';

@Component({
  selector: 'dv-shared-ui-version-text',
  imports: [],
  templateUrl: './shared-ui-version-text.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiVersionTextComponent {
  @Input({ required: true }) version!: {
    frontend: string;
    backend?: string;
    sameVersion: boolean;
  };

  @HostBinding('class') defaultClasses =
    'tw:absolute tw:mr-2 tw:bottom-0 tw:m-0 tw:right-0 tw:text-end';
}
