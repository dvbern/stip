import { Pipe, PipeTransform } from '@angular/core';

type IsTranslated<K> = K extends `${string}De` | `${string}Fr` ? K : never;
type NameOfTranslated<K> = K extends `${infer Name}De` | `${infer Name}Fr`
  ? Name
  : never;
type OnlyKeysWithDeOrFr<T> = {
  [K in keyof T]: IsTranslated<K>;
}[keyof T];

@Pipe({
  standalone: true,
  name: 'translatedProp',
})
export class TranslatedPropertyPipe implements PipeTransform {
  transform<T extends Record<string, unknown>, K extends OnlyKeysWithDeOrFr<T>>(
    value: T,
    property: NameOfTranslated<K>,
    language: string,
  ): T[K] {
    const lang = prepareLanguage(language);
    const translatedProperty = `${property}${lang}` as K;
    if (!(translatedProperty in value)) {
      throw new Error(
        `Property '${translatedProperty}' does not exist on given value`,
      );
    }
    return value[translatedProperty];
  }
}

export function prepareLanguage(lang: string): 'De' | 'Fr' {
  const deOrFr = lang[0].toUpperCase() + lang.slice(1);
  if (deOrFr !== 'De' && deOrFr !== 'Fr') {
    throw new Error(`Invalid language '${lang}', valid are 'De' and 'Fr'`);
  }
  return deOrFr;
}
