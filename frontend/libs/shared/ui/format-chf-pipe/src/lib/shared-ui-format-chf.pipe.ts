import { Pipe, PipeTransform } from '@angular/core';

import { isDefined } from '@dv/shared/model/type-util';
import { toFormatedNumber } from '@dv/shared/util/maskito-util';

type Value = string | number | undefined;

/**
 * Formats a number to a CHF currency string.
 * @param {Value}value The value to format. has to be a number.
 * @param {boolean}[addNegativeSign=true] If true, adds a - sign in front of a negative number. default is true.
 * @returns {string} The formatted CHF currency string.
 * @example {{ 1234.56 | formatChf }} => 1'234
 * {{ -1234 | formatChf }} => - 1'234
 * {{ -1234 | formatChf: false }} => 1'234
 * @description use this pipe for total amounts, where the negative sign is not needed.
 */
@Pipe({
  name: 'formatChf',
  standalone: true,
})
export class SharedUiFormatChfPipe implements PipeTransform {
  transform(value: Value, addNegativeSign = true): string {
    if (!isDefined(value)) {
      return '';
    }
    if (addNegativeSign && +value > 0) {
      return `${toFormatedNumber(+value)}`;
    }
    if (addNegativeSign && +value < 0) {
      return `- ${toFormatedNumber(Math.abs(+value))}`;
    }
    return `${toFormatedNumber(Math.abs(+value))}`;
  }
}

/**
 * Formats a number to a CHF currency string, always showing a positive sign if specified.
 * @param {Value} value - The value to format. Has to be a number.
 * @param {boolean} [alwaysPostive=false] - If true, always shows a positive sign in front of any number, including negative ones. default is false.
 * @returns {string} The formatted CHF currency string.
 * @example {{ 1234 | formatChfPositive }} => + 1'234
 * {{ -1234 | formatChfPositive: true }} => + 1'234
 * {{ -1234 | formatChfPositive }} => - 1'234
 * @description use this pipe for amounts, where the positive sign is needed to indicate an addition operation was performed.
 */
@Pipe({
  name: 'formatChfPositive',
  standalone: true,
})
export class SharedUiFormatChfPositivePipe implements PipeTransform {
  transform(value: Value, alwaysPostive = false): string {
    if (value === undefined) {
      return '';
    }
    if (alwaysPostive && +value < 0) {
      return `+ ${toFormatedNumber(Math.abs(+value))}`;
    }
    if (+value > 0) {
      return `+ ${toFormatedNumber(+value)}`;
    }
    if (+value === 0) {
      return '0';
    }
    return `- ${toFormatedNumber(Math.abs(+value))}`;
  }
}

/**
 * Formats a number to a CHF currency string, always showing a negative sign if specified.
 * @param {Value} value - The value to format. Has to be a number.
 * @param {boolean} [alwaysNegative=false] - If true, always shows a negative sign in front of any number, including positive ones. default is false.
 * @returns {string} The formatted CHF currency string.
 * @example {{ 1234 | formatChfNegative }} => 1'234
 * {{ -1234 | formatChfNegative }} => - 1'234
 * {{ 1234 | formatChfNegative: true }} => - 1'234
 * @description use this pipe for amounts, where the negative sign is needed to indicate a subtraction operation was performed.
 */
@Pipe({
  name: 'formatChfNegative',
  standalone: true,
})
export class SharedUiFormatChfNegativePipe implements PipeTransform {
  transform(value: Value, alwaysNegative = false): string {
    if (value === undefined) {
      return '';
    }
    if (alwaysNegative && +value > 0) {
      return `- ${toFormatedNumber(+value)}`;
    }
    if (+value < 0) {
      return `- ${toFormatedNumber(Math.abs(+value))}`;
    }
    return `${toFormatedNumber(+value)}`;
  }
}
