import { Pipe, PipeTransform } from '@angular/core';

import { isDefined } from '@dv/shared/model/type-util';

type PadTextOptions = {
  padChar?: string;
  direction?: 'left' | 'right';
};

@Pipe({
  name: 'dvPadText',
  standalone: true,
})
export class SharedUiPadTextPipeComponent implements PipeTransform {
  transform(
    value: string | number | undefined | null,
    length: number,
    options: PadTextOptions = { direction: 'left', padChar: '0' },
  ): string | undefined | null {
    if (!isDefined(value) || value === '') {
      return value;
    }
    if ((typeof value !== 'string' && !Number.isNaN(value)) || length < 0) {
      throw new Error(
        'Invalid input: value must be a string or a number, and length must be a non-negative integer.',
      );
    }
    const { padChar, direction } = options;
    const paddedValue =
      direction === 'left'
        ? value.toString().padStart(length, padChar)
        : value.toString().padEnd(length, padChar);
    return paddedValue;
  }
}
