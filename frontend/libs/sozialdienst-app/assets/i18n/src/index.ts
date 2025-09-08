import { AssertMatchAndMergeTranslations } from '@dv/shared/model/type-util';

import type de from './de.json';
import type fr from './fr.json';

export type SozialdienstAppTranslationKey = AssertMatchAndMergeTranslations<
  typeof de,
  typeof fr
>;

export const translatableSd = <const T extends SozialdienstAppTranslationKey>(
  value: T,
) => value;
