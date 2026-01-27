import { AssertMatchAndMergeTranslations } from '@dv/shared/model/type-util';

import type de from './de.json';
import type fr from './fr.json';

export type DemoDataAppTranslationKey = AssertMatchAndMergeTranslations<
  typeof de,
  typeof fr
>;

export const translatableDemo = <const T extends DemoDataAppTranslationKey>(
  value: T,
) => value;
