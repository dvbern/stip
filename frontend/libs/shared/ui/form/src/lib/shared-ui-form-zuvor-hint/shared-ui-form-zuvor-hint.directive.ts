import { Directive, Input, ViewContainerRef, inject } from '@angular/core';

import { FormularChangeTypes } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { SharedUiZuvorHintComponent } from './shared-ui-form-zuvor-hint.template';

/**
 * @description
 * Directive to show a the previous values of Formfields.
 * - Must be placed on a mat-hint component.
 * - Must be used in combination within a mat-form-field.
 * - If there is another hint, the hint will be replaced if there is a previous value.
 * Ohter hints must be placed after this hint element.
 * - The following hint must also have the attribute `align="end"` so material does not throw an error.
 *
 * @example
 * <mat-hint
 *  *dvZuvorHint="previousValue"
 *  data-testid="form-person-sozialversicherungsnummer-zuvor-hint"
 *  class="zuvor-hint"
 * >
 * </mat-hint>
 * <mat-hint align="end" translate>shared.form.einnahmenkosten.nettoerwerbseinkommen.info</mat-hint>
 */
@Directive({
  selector: '[dvZuvorHint]',
  standalone: true,
})
export class SharedUiZuvorHintDirective {
  private viewContainerRef = inject(ViewContainerRef);

  @Input() set dvZuvorHint(value: FormularChangeTypes) {
    if (isDefined(value)) {
      this.viewContainerRef.clear();
      const compRef = this.viewContainerRef.createComponent(
        SharedUiZuvorHintComponent,
      );
      compRef.setInput(<keyof SharedUiZuvorHintComponent>'zuvorSig', value);
    } else {
      this.viewContainerRef.clear();
    }
  }
}
