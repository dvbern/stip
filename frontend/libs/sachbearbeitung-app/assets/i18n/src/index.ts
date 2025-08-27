import {
  AssertMatchAndMergeTranslations,
  type,
} from '@dv/shared/model/type-util';

import type de from './de.json';
import type fr from './fr.json';

export type SachbearbeitungAppTranslationKey = AssertMatchAndMergeTranslations<
  typeof de,
  typeof fr
>;

export const translatableSb = type<SachbearbeitungAppTranslationKey>;
