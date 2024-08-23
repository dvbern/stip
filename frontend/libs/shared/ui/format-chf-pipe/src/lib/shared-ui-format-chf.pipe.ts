import { Pipe, PipeTransform } from '@angular/core';

import { toFormatedNumber } from '@dv/shared/util/maskito-util';

type Value = string | number | undefined;

@Pipe({
  name: 'formatChf',
  standalone: true,
})
export class SharedUiFormatChfPipe implements PipeTransform {
  transform(value: Value, addSign = true): string {
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
  transform(value: Value): string {
    if (value === undefined) {
      return '';
    }
    if (+value >= 0) {
      return `${toFormatedNumber(+value)}`;
    }
    return `- ${toFormatedNumber(+value)}`;
  }
}

@Pipe({
  name: 'formatNoChf',
  standalone: true,
})
export class SharedUiFormatNoChfPipe implements PipeTransform {
  transform(value: Value): string {
    if (value === undefined) {
      return '';
    }
    return toFormatedNumber(+value);
  }
}
