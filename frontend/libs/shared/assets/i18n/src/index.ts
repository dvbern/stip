import { ContractTranslations } from '@dv/contract/translations';

import { AssertMatchAndMergeTranslations } from '@dv/shared/model/type-util';

import type de from './shared.de.json';
import type fr from './shared.fr.json';

export type SharedTranslationKey =
  | AssertMatchAndMergeTranslations<typeof de, typeof fr>
  | AssertMatchAndMergeTranslations<
      ContractTranslations['de'],
      ContractTranslations['fr']
    >;

export const translatableShared = <const T extends SharedTranslationKey>(
  value: T,
) => value;
