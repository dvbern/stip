/* eslint-disable @angular-eslint/no-input-rename */
import {
  Directive,
  TemplateRef,
  ViewContainerRef,
  effect,
  inject,
  input,
} from '@angular/core';

@Directive({
  selector: '[dvHideZero]',
  standalone: true,
})
export class HideZeroDirective {
  private hasView = false;

  templateRef = inject(TemplateRef);
  viewContainer = inject(ViewContainerRef);

  hideSig = input<boolean>(false, { alias: 'dvHideZero' });
  valueSig = input.required<number | undefined | null>({
    alias: 'dvHideZeroValue',
  });

  constructor() {
    effect(() => {
      const hide = this.hideSig();
      const value = this.valueSig();

      if (hide && value === 0) {
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
