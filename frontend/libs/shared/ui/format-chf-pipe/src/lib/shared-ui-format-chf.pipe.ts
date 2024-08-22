import { Pipe, PipeTransform } from '@angular/core';

import { toFormatedNumber } from '@dv/shared/util/maskito-util';

@Pipe({
  name: 'formatChf',
  standalone: true,
})
export class SharedUiFormatChfPipe implements PipeTransform {
  transform(value: string | number | undefined, addSign = true): string {
    if (value === undefined) {
      return '';
    }
    if (!addSign || +value === 0) {
      return `${toFormatedNumber(+value)}`;
    }
    if (+value > 0) {
      return `+ ${toFormatedNumber(+value)}`;
    }
    return `- ${toFormatedNumber(+value)}`;
  }
}

@Pipe({
  name: 'formatChfNegative',
  standalone: true,
})
export class SharedUiFormatChfNegativePipe implements PipeTransform {
  transform(value: string | number | undefined): string {
    if (value === undefined) {
      return '';
    }
    if (+value >= 0) {
      return `${toFormatedNumber(+value)}`;
    }
    return `- ${toFormatedNumber(+value)}`;
  }
}
