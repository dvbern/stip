import { toSignal } from '@angular/core/rxjs-interop';
import { TranslateService } from '@ngx-translate/core';
import { map } from 'rxjs';

import { isDefined } from './shared-model-type-util-guard';
import { OnlyString } from './shared-model-type-util-string';
export type AssertMatchAndMergeTranslations<
  De extends Record<string, unknown>,
  Fr extends Record<keyof De, unknown> & {
    [key in keyof Fr]: key extends keyof De
      ? Fr[key]
      : `De is missing key: ${OnlyString<key>}`;
  },
> = (keyof Fr | keyof De) & string;

export type IsTranslated<K> = K extends `${string}De` | `${string}Fr`
  ? K
  : never;
export type NameOfTranslated<K> = K extends
  | `${infer Name}De`
  | `${infer Name}Fr`
  ? Name
  : never;
export type OnlyKeysWithDeOrFr<T> = {
  [K in keyof T]: IsTranslated<K>;
}[keyof T];
export type KnownLanguage = 'de' | 'fr';

export function prepareLanguage(lang: string): Capitalize<KnownLanguage> {
  const deOrFr = lang[0].toUpperCase() + lang.slice(1);
  if (deOrFr !== 'De' && deOrFr !== 'Fr') {
    throw new Error(`Invalid language '${lang}', valid are 'De' and 'Fr'`);
  }
  return deOrFr;
}

export function getCorrectPropertyName<Property extends string>(
  property: Property,
  language: string,
): `${Property}${Capitalize<KnownLanguage>}` {
  const lang = prepareLanguage(language);
  return `${property}${lang}`;
}

export function getCurrentLanguageSig(translate: TranslateService) {
  return toSignal(
    translate.onLangChange.pipe(
      map(() => isKnownLanguage(translate.currentLang)),
    ),
    {
      initialValue: isKnownLanguage(translate.currentLang),
    },
  );
}

export function isKnownLanguage(language: string): KnownLanguage {
  if (language === 'de' || language === 'fr') {
    return language;
  }

  throw new Error(
    `Unknown translation language: ${language}. Expected 'de' or 'fr'.`,
  );
}

export const getTranslatableProp = <
  T extends { [key in K]: unknown },
  K extends OnlyKeysWithDeOrFr<T>,
>(
  value: Partial<T> | undefined,
  property: NameOfTranslated<K>,
  language: string,
): T[K] | null => {
  if (!isDefined(value)) {
    return null;
  }
  const lang = prepareLanguage(language);
  const translatedProperty = `${property}${lang}` as K;
  if (!(translatedProperty in value)) {
    throw new Error(
      `Property '${translatedProperty}' does not exist on given value`,
    );
  }
  if (!isDefined(value[translatedProperty])) {
    return null;
  }
  return value[translatedProperty];
};
