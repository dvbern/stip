import { Pipe, PipeTransform } from '@angular/core';
import { format } from 'date-fns/format';

import { isDefined } from '@dv/shared/model/type-util';

@Pipe({
  name: 'dvAppDate',
  standalone: true,
})
export class SharedUiAppDatePipe implements PipeTransform {
  transform<T extends string | undefined | null>(
    value: T | number | Date,
    dateFormat: string,
  ): T | string {
    if (!isDefined(value)) {
      return value;
    }
    return format(value, dateFormat);
  }
}
