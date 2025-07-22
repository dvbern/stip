import { AssertMatchAndMergeTranslations } from '@dv/shared/model/type-util';

import type de from './shared.de.json';
import type fr from './shared.fr.json';

export type SharedTranslationKey = AssertMatchAndMergeTranslations<
  typeof de,
  typeof fr
>;
