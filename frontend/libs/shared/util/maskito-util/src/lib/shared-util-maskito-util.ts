import {
  MaskitoOptions,
  maskitoTransform,
  maskitoUpdateElement,
} from '@maskito/core';
import {
  maskitoCaretGuard,
  maskitoEventHandler,
  maskitoNumberOptionsGenerator,
} from '@maskito/kit';

export const NUMBER_THOUSAND_SEPARATOR = "'";

export const postfix = '%';
const { plugins, ...numberOptions } = maskitoNumberOptionsGenerator({
  postfix,
  min: 0,
  max: 100,
  precision: 2,
});

export const maskitoPercent = {
  ...numberOptions,
  plugins: [
    ...plugins,
    // Forbids caret to be placed after postfix
    maskitoCaretGuard((value) => [0, value.length - 1]),
    maskitoEventHandler('blur', (element) => {
      maskitoUpdateElement(element, element.value.replace(/ /g, ''));
    }),
  ],
} satisfies MaskitoOptions;

export const maskitoNumberWithNegative = maskitoNumberOptionsGenerator({
  thousandSeparator: NUMBER_THOUSAND_SEPARATOR,
});

export const maskitoNumber = maskitoNumberOptionsGenerator({
  min: 0,
  thousandSeparator: NUMBER_THOUSAND_SEPARATOR,
});

export const maskitoPositiveNumber = maskitoNumberOptionsGenerator({
  min: 1,
  thousandSeparator: NUMBER_THOUSAND_SEPARATOR,
});

export const maskitoYear = maskitoNumberOptionsGenerator({
  min: 0,
  precision: 0,
  thousandSeparator: '',
});

export function maskitoMaxNumber(max: number) {
  return maskitoNumberOptionsGenerator({
    min: 0,
    max: max,
    thousandSeparator: NUMBER_THOUSAND_SEPARATOR,
  });
}

export function fromFormatedNumber(formatedNumber: string): number;
export function fromFormatedNumber(
  formatedNumber: string | null,
): number | null;
export function fromFormatedNumber(
  formatedNumber: string | undefined,
): number | undefined;
export function fromFormatedNumber(
  formatedNumber: string | null | undefined,
): number | null | undefined {
  if (formatedNumber === '' || formatedNumber?.trim() === '') {
    return null;
  }
  return formatedNumber != null
    ? +formatedNumber
        .replaceAll(NUMBER_THOUSAND_SEPARATOR, '')
        .replaceAll('−', '-')
    : formatedNumber;
}

export function toFormatedNumber(number: number): string {
  return maskitoTransform(number.toString(), maskitoNumber);
}
