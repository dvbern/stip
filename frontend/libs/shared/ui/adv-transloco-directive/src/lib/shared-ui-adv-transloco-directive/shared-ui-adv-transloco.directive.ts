import { Directive } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';

// eslint-disable-next-line @typescript-eslint/no-explicit-any
type HashMap<T = any> = Record<string, T>;
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
