import { Pipe, PipeTransform } from '@angular/core';

import { toFormatedNumber } from '@dv/shared/util/maskito-util';

@Pipe({
  name: 'formatChf',
  standalone: true,
})
export class SharedUiFormatChfPipe implements PipeTransform {
  transform(value: string | number | undefined): string {
    if (value === undefined) {
      return '';
    }
    if (+value > 0) {
      return `+ ${toFormatedNumber(+value)} CHF`;
    }
    if (+value < 0) {
      return `- ${toFormatedNumber(+value)} CHF`;
    }
    return `${toFormatedNumber(+value)} CHF`;
  }
}
