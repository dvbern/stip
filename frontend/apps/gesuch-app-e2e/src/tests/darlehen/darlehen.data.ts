import { DarlehenGrund } from '@dv/shared/model/gesuch';
import type {
  DarlehenGsValues,
  DarlehenSbValues,
} from '@dv/shared/util-fn/e2e-util';

export {
  ausbildungValues,
  createZahlungsverbindungUpdateFn,
  gesuchFormularUpdateFn,
} from '../tranchen/tranchen.data';

export const darlehenGs: DarlehenGsValues = {
  betragGewuenscht: 5000,
  schulden: 1200,
  anzahlBetreibungen: 0,
};

/**
 * Grund that does not require an additional document to be uploaded.
 */
export const grundOhneDokument: DarlehenGrund = 'AUSBILDUNG_ZWOELF_JAHRE';

/**
 * Grund that requires an additional document to be uploaded.
 */
export const grundMitDokument: DarlehenGrund = 'HOHE_GEBUEHREN';

export const darlehenSbGewaehren: DarlehenSbValues = {
  gewaehren: true,
  betrag: 4000,
  kommentar: 'E2E Darlehen gewährt Kommentar',
};

export const darlehenSbAblehnen: DarlehenSbValues = {
  gewaehren: false,
  kommentar: 'E2E Darlehen abgelehnt Kommentar',
};
