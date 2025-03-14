import { Geschwister } from '@dv/shared/model/gesuch';
import { specificYearsAgo } from '@dv/shared/util-fn/e2e-util';

/**
 * Bruder in eingenem Haushalt
 */
export const bruder: Geschwister = {
  nachname: 'Tester',
  vorname: 'Geschwister1',
  geburtsdatum: `01.01.${specificYearsAgo(19)}`,
  wohnsitz: 'EIGENER_HAUSHALT',
  ausbildungssituation: 'KEINE',
  id: '',
};
