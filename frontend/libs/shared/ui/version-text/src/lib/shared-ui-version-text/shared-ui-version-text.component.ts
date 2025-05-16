import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';

@Component({
  selector: 'dv-shared-ui-version-text',
  imports: [CommonModule],
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
    'position-absolute me-2 bottom-0 end-0 text-end';
}
