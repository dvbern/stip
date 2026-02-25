import { Directive } from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { DemoDataAppTranslationKey } from '@dv/demo-data-app/assets/i18n';
import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { TranslocoHashMap } from '@dv/shared/model/type-util';

type TranslateFn<T extends DemoDataAppTranslationKey | SharedTranslationKey> = (
  key: T,
  params?: TranslocoHashMap,
) => string;
interface ViewContext {
  $implicit: TranslateFn<DemoDataAppTranslationKey | SharedTranslationKey>;
  currentLang: string;
}

@Directive({
  selector: '[dvTranslocoDemo]',
})
// @ts-expect-error: ts(2417) - Apply strongly typed type guard to the directive
export class DemoDataAppUiAdvTranslocoDirective extends TranslocoDirective {
  static override ngTemplateContextGuard(
    _dir: DemoDataAppUiAdvTranslocoDirective,
    ctx: unknown,
  ): ctx is ViewContext {
    return true;
  }
}
