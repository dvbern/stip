import { AssertMatchAndMergeTranslations } from '@dv/shared/model/type-util';

import type de from './shared.de.json';
import type fr from './shared.fr.json';

export type TranslationKey = AssertMatchAndMergeTranslations<
  typeof de,
  typeof fr
>;
export type SharedT<T extends string> = T extends TranslationKey ? T : never;
