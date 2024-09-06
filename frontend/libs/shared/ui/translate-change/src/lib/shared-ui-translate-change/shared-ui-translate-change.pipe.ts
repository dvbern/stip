import { Pipe, PipeTransform } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

import { FormularChangeTypes } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/util-fn/type-guards';

const PLACEHOLDER = '$VALUE';

@Pipe({
  standalone: true,
  name: 'translateChange',
  pure: false,
})
export class SharedUiTranslateChangePipe
  extends TranslatePipe
  implements PipeTransform
{
  override transform(
    value: FormularChangeTypes,
    translationKey: `${string}${typeof PLACEHOLDER}${string}`,
  ) {
    if (value === '') {
      return '';
    }
    if (isDefined(value)) {
      return super.transform(
        translationKey.replace(`${PLACEHOLDER}`, value.toString()),
      );
    }
    return null;
  }
}
