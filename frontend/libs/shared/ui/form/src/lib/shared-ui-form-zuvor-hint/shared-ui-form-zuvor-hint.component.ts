import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

import { FormularChangeTypes } from '@dv/shared/model/gesuch-form';

import { SharedUiZuvorHintDirective } from './shared-ui-form-zuvor-hint.directive';

/**
 * A component used to show a hint if a value has changed.
 *
 * Usable for form controls which are not part of a `mat-form-field`.
 *
 * @example
 * <mat-checkbox
 *   formControlName="identischerZivilrechtlicherWohnsitz"
 *   >{{ 'shared.form.shared.identical-civil-residence.label' | translate }}</mat-checkbox
 * >
 * <dv-shared-ui-form-zuvor-hint
 *   [changes]="view.formChanges?.identischerZivilrechtlicherWohnsitz | translateChange: 'shared.form.zuvor.checkbox.$VALUE'"
 * ></dv-shared-ui-form-zuvor-hint>
 *
 * @example
 *
 * <mat-radio-button data-testid="no" [value]="false">{{
 *   'shared.form.radio.no' | translate
 * }}</mat-radio-button>
 * <dv-shared-ui-form-zuvor-hint
 *   [changes]="view.formChanges?.sozialhilfebeitraege | translateChange: 'shared.form.zuvor.radio.$VALUE'"
 * ></dv-shared-ui-form-zuvor-hint>
 */
@Component({
  selector: 'dv-shared-ui-form-zuvor-hint',
  imports: [SharedUiZuvorHintDirective],
  template: `
    <div class="form-hints">
      <span *dvZuvorHint="changes"></span>
    </div>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFormZuvorHintComponent {
  @Input({ required: true }) changes: FormularChangeTypes | undefined =
    undefined;
}
