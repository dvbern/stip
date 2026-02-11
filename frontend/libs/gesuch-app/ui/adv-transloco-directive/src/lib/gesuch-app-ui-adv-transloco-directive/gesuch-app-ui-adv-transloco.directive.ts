import { Directive } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { GesuchAppTranslationKey } from '@dv/gesuch-app/assets/i18n';
import { SharedTranslationKey } from '@dv/shared/assets/i18n';

// eslint-disable-next-line @typescript-eslint/no-explicit-any
type HashMap<T = any> = Record<string, T>;
type TranslateFn<T extends GesuchAppTranslationKey | SharedTranslationKey> = (
  key: T,
  params?: HashMap,
) => string;
interface ViewContext {
  $implicit: TranslateFn<GesuchAppTranslationKey | SharedTranslationKey>;
  currentLang: string;
}

@Directive({
  selector: '[dvTranslocoGs]',
})
// @ts-expect-error: ts(2417) - Apply strongly typed type guard to the directive
export class GesuchAppUiAdvTranslocoDirective extends TranslocoDirective {
  static override ngTemplateContextGuard(
    _dir: GesuchAppUiAdvTranslocoDirective,
    ctx: unknown,
  ): ctx is ViewContext {
    return true;
  }
}
