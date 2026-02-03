import { Directive } from '@angular/core';
import { HashMap, TranslocoDirective } from '@jsverse/transloco';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { SozialdienstAppTranslationKey } from '@dv/sozialdienst-app/assets/i18n';

type TranslateFn<
  T extends SozialdienstAppTranslationKey | SharedTranslationKey,
> = (key: T, params?: HashMap) => string;
interface ViewContext {
  $implicit: TranslateFn<SozialdienstAppTranslationKey | SharedTranslationKey>;
  currentLang: string;
}

@Directive({
  selector: '[dvTranslocoSd]',
})
// @ts-expect-error: ts(2417) - Apply strongly typed type guard to the directive
export class SozialdienstAppUiAdvTranslocoDirective extends TranslocoDirective {
  static override ngTemplateContextGuard(
    _dir: SozialdienstAppUiAdvTranslocoDirective,
    ctx: unknown,
  ): ctx is ViewContext {
    return true;
  }
}
