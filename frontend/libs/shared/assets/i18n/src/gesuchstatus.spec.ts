import { Gesuchstatus } from '@dv/shared/model/gesuch';

import { default as translationsDe } from './shared.de.json';
import { default as translationsFr } from './shared.fr.json';

const StatusKeyPrefix = 'sachbearbeitung-app.gesuch.status.contract.';
type TranslationKeys = keyof typeof translationsDe;
type StatusTranslationKeys = Extract<
  TranslationKeys,
  `${typeof StatusKeyPrefix}${string}`
>;

describe('GesuchStatus', () => {
  it.each([
    ['DE', translationsDe],
    ['FR', translationsFr],
  ])(
    'should have OpenAPI conform translations keys [%s]',
    (_, translations) => {
      const statusKeys = (
        Object.keys(translations) as TranslationKeys[]
      ).filter(isStatusKey);

      const validStatus = Object.keys(Gesuchstatus) as Gesuchstatus[];
      statusKeys.forEach((key) => {
        expect(validStatus).toContain(key.replace(StatusKeyPrefix, ''));
      });
    },
  );
});

const isStatusKey = (key: TranslationKeys): key is StatusTranslationKeys => {
  return key.startsWith(StatusKeyPrefix);
};
