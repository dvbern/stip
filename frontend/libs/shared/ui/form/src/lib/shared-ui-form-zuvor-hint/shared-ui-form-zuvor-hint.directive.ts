import { Directive, Input, ViewContainerRef, inject } from '@angular/core';

import { FormularChangeTypes } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { SharedUiZuvorHintComponent } from './shared-ui-form-zuvor-hint.template';

/**
 * A directive to show a hint if a value has changed.
 *
 * It should be placed on a `mat-hint` element and the mat-hint should be the **FIRST** child of type `mat-hint` in a `mat-form-field`.
 *
 * @example
 * <mat-hint *dvZuvorHint="previousValue"></mat-hint>
 * <mat-hint translate>shared.form.einnahmenkosten.nettoerwerbseinkommen.info</mat-hint>
 *
 * @example <caption>Translated or values which need parsing</caption>
 * <mat-hint
 *   *dvZuvorHint="view.formChanges?.anrede | lowercase | translateChange: 'shared.form.select.salutation.$VALUE'"
 * ></mat-hint>
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
