import {
  ComponentRef,
  Directive,
  ViewContainerRef,
  effect,
  inject,
  input,
} from '@angular/core';

import { FormularChangeTypes } from '@dv/shared/model/gesuch-form';
import { toFormatedNumber } from '@dv/shared/util/maskito-util';
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
 *
 * @example <caption>With a suffix</caption>
 * <mat-hint *dvZuvorHint="view.formChanges?.wohnsitzAnteilMutter; suffix: '%'" translate>
 */
@Directive({
  selector: '[dvZuvorHint]',
  standalone: true,
})
export class SharedUiZuvorHintDirective {
  private viewContainerRef = inject(ViewContainerRef);

  dvZuvorHintSuffixSig = input<string>('', { alias: 'dvZuvorHintSuffix' });
  dvZuvorHintSig = input<FormularChangeTypes>(undefined, {
    alias: 'dvZuvorHint',
  });

  constructor() {
    let componentRef: ComponentRef<SharedUiZuvorHintComponent> | undefined =
      undefined;
    effect(() => {
      let value = this.dvZuvorHintSig();
      const suffix = this.dvZuvorHintSuffixSig();

      if (isDefined(value)) {
        if (!componentRef) {
          this.viewContainerRef.clear();
          componentRef = this.viewContainerRef.createComponent(
            SharedUiZuvorHintComponent,
          );
        }
        if (typeof value === 'number') {
          value = toFormatedNumber(value);
        }
        componentRef.setInput(
          <keyof SharedUiZuvorHintComponent>'zuvorSig',
          value + suffix,
        );
      } else {
        componentRef?.destroy();
        componentRef = undefined;
        this.viewContainerRef.clear();
      }
    });
  }
}