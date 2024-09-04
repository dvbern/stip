import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

import { FormularChangeTypes } from '@dv/shared/model/gesuch-form';

import { SharedUiZuvorHintDirective } from './shared-ui-form-zuvor-hint.directive';

@Component({
  selector: 'dv-shared-ui-form-zuvor-hint',
  imports: [SharedUiZuvorHintDirective],
  standalone: true,
  template: `
    <div class="form-hints">
      <span *dvZuvorHint="changes"></span>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFormZuvorHintComponent {
  @Input() changes: FormularChangeTypes | undefined = undefined;
}
