import { AssertMatchAndMergeTranslations } from '@dv/shared/model/type-util';

import type de from './i18n/de.json';
import type fr from './i18n/fr.json';

export type TranslationKey = AssertMatchAndMergeTranslations<
  typeof de,
  typeof fr
>;
export type GsT<T extends string> = T extends TranslationKey ? T : never;
