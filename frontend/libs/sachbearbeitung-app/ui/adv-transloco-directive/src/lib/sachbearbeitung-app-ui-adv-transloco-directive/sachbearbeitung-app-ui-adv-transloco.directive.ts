import { Directive } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { TranslocoHashMap } from '@dv/shared/model/type-util';

type TranslateFn<
  T extends SachbearbeitungAppTranslationKey | SharedTranslationKey,
> = (key: T, params?: TranslocoHashMap) => string;
interface ViewContext {
  $implicit: TranslateFn<
    SachbearbeitungAppTranslationKey | SharedTranslationKey
  >;
  currentLang: string;
}

@Directive({
  selector: '[dvTranslocoSb]',
})
// @ts-expect-error: ts(2417) - Apply strongly typed type guard to the directive
export class SachbearbeitungAppUiAdvTranslocoDirective extends TranslocoDirective {
  static override ngTemplateContextGuard(
    _dir: SachbearbeitungAppUiAdvTranslocoDirective,
    ctx: unknown,
  ): ctx is ViewContext {
    return true;
  }
}
