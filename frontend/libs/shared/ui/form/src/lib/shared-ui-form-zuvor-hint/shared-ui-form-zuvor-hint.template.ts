import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  inject,
  input,
} from '@angular/core';
import { MatHint } from '@angular/material/form-field';

// eslint-disable-next-line @typescript-eslint/no-unused-vars -- Used by doc
import type { SharedUiZuvorHintDirective } from './shared-ui-form-zuvor-hint.directive';

/**
 * Used by {@link SharedUiZuvorHintDirective} to show changed values.
 */
@Component({
  selector: 'dv-shared-ui-zuvor-hint',
  standalone: true,
  template: '{{zuvorSig()}}',
  hostDirectives: [MatHint],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiZuvorHintComponent {
  zuvorSig = input<string>();
  private matHint = inject(MatHint, { self: true });

  @HostBinding('class') klass = 'zuvor-hint';

  constructor() {
    this.matHint.align = 'end';
  }
}
