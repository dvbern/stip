import { Pipe, PipeTransform } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

import { FormularChangeTypes } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';

const PLACEHOLDER = '$VALUE';
type Placeholder = typeof PLACEHOLDER;

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
    translationKey:
      | `shared.form.zuvor.checkbox.${Placeholder}`
      | `shared.form.zuvor.radio.${Placeholder}`
      | (`${string}${typeof PLACEHOLDER}${string}` & {}),
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
