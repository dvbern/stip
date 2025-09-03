import { AssertMatchAndMergeTranslations } from '@dv/shared/model/type-util';

import type de from './de.json';
import type fr from './fr.json';

export type GesuchAppTranslationKey = AssertMatchAndMergeTranslations<
  typeof de,
  typeof fr
>;

export const translatableGs = <const T extends GesuchAppTranslationKey>(
  value: T,
) => value;
