import { Directive, TemplateRef, inject, input } from '@angular/core';

import { LookupType } from '@dv/shared/model/select-search';

export interface SelectTemplateContext<T extends LookupType> {
  $implicit: T;
}

@Directive({
  selector: '[dvSearchOptionLabel]',
  standalone: true,
})
export class SharedUiSearchOptionLabelDirective<T extends LookupType> {
  dvSearchOptionLabel = input.required<T[]>();

  public templateRef = inject(TemplateRef<T>);

  static ngTemplateContextGuard<T extends LookupType>(
    dir: SharedUiSearchOptionLabelDirective<T>,
    ctx: any,
  ): ctx is SelectTemplateContext<T> {
    // As before the guard body is not used at runtime, and included only to avoid
    // TypeScript errors.
    return true;
  }
}
