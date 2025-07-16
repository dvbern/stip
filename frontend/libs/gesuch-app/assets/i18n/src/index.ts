import { AssertMatchAndMergeTranslations } from '@dv/shared/model/type-util';

import type de from './de.json';
import type fr from './fr.json';

export type GesuchAppTranslationKey = AssertMatchAndMergeTranslations<
  typeof de,
  typeof fr
>;
export type SharedT<T extends string> = T extends GesuchAppTranslationKey
  ? T
  : never;
