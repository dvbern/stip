import { Pipe, PipeTransform, inject } from '@angular/core';
import { TranslocoService } from '@jsverse/transloco';
import { of } from 'rxjs';

import { FormularChangeTypes } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';

const PLACEHOLDER = '$VALUE';
type Placeholder = typeof PLACEHOLDER;

@Pipe({
  standalone: true,
  name: 'dvTranslateChange',
  pure: false,
})
export class SharedUiTranslateChangePipe implements PipeTransform {
  private translate = inject(TranslocoService);
  transform(
    value: FormularChangeTypes,
    translationKey:
      | `shared.form.zuvor.checkbox.${Placeholder}`
      | `shared.form.zuvor.radio.${Placeholder}`
      | (`${string}${typeof PLACEHOLDER}${string}` & {}),
  ) {
    if (!isDefined(value)) {
      return of(value);
    }
    if (value === '') {
      return of('');
    }
    return this.translate.selectTranslate(
      translationKey.replace(`${PLACEHOLDER}`, value.toString()),
    );
  }
}
