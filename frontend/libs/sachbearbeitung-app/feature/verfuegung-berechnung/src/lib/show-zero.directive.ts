/* eslint-disable @angular-eslint/no-input-rename */
import {
  Directive,
  TemplateRef,
  ViewContainerRef,
  effect,
  inject,
  input,
} from '@angular/core';

// todo: es macht mehr sinn, als default keine null anzuzeigen, weshalb es heissen sollte "null werte anzeigen"
// und wir die direktive dann "dvShowZero" nennen. @mada fragen.

@Directive({
  selector: '[dvShowZero]',
  standalone: true,
})
export class ShowZeroDirective {
  private hasView = false;

  templateRef = inject(TemplateRef);
  viewContainer = inject(ViewContainerRef);

  showSig = input<boolean>(false, { alias: 'dvShowZero' });
  valueSig = input.required<number | undefined | null>({
    alias: 'dvShowZeroValue',
  });

  constructor() {
    effect(() => {
      const show = this.showSig();
      const value = this.valueSig();

      if (!show && value === 0) {
        if (this.hasView) {
          this.viewContainer.clear();
          this.hasView = false;
        }
      } else {
        if (!this.hasView) {
          this.viewContainer.createEmbeddedView(this.templateRef);
          this.hasView = true;
        }
      }
    });
  }
}
