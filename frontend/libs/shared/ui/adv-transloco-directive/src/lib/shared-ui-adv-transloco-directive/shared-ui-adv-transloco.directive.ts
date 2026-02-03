import { Directive } from '@angular/core';
import { HashMap, TranslocoDirective } from '@jsverse/transloco';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';

type TranslateFn<T extends SharedTranslationKey> = (
  key: T,
  params?: HashMap,
) => string;
interface ViewContext {
  $implicit: TranslateFn<SharedTranslationKey>;
  currentLang: string;
}

@Directive({
  selector: '[dvTranslocoShared]',
})
// @ts-expect-error: ts(2417) - Apply strongly typed type guard to the directive
export class SharedUiAdvTranslocoDirective extends TranslocoDirective {
  static override ngTemplateContextGuard(
    _dir: SharedUiAdvTranslocoDirective,
    ctx: unknown,
  ): ctx is ViewContext {
    return true;
  }
}
