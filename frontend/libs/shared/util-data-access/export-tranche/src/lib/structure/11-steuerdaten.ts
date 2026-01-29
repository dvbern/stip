import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { Steuerdaten } from '@dv/shared/model/gesuch';

import { getTable, getTitle, getValueList } from './generic';
import { _t } from '../utils/helpers';

export const getSteuerdatens = (
  t: TranslocoService,
  steuerdaten: Steuerdaten[],
): Content => {
  if (!steuerdaten?.length) {
    return [];
  }

  return [
    steuerdaten.map((steuerdaten, i) => [
      getTitle(
        t,
        `shared.eltern-steuerdaten.title.${steuerdaten.steuerdatenTyp}`,
        i == 0 ? { pageBreak: 'before' } : { marginTop: 30 },
      ),
      getTable([
        ...getValueList(
          t,
          [
            ['totalEinkuenfte', steuerdaten.totalEinkuenfte],
            ['eigenmietwert', steuerdaten.eigenmietwert],
            [
              'arbeitsverhaeltnis',
              _t(
                t,
                `shared.form.eltern-steuerdaten.arbeitsverhaeltnis.${steuerdaten.isArbeitsverhaeltnisSelbstaendig}`,
              ),
            ],
            ['saeule3a', steuerdaten.saeule3a],
            ['saeule2', steuerdaten.saeule2],
            ['vermoegen', steuerdaten.vermoegen],
            ['steuernKantonGemeinde', steuerdaten.steuernKantonGemeinde],
            ['steuernBund', steuerdaten.steuernBund],
            ['fahrkosten', steuerdaten.fahrkosten],
            ['fahrkostenPartner', steuerdaten.fahrkostenPartner],
            ['verpflegung', steuerdaten.verpflegung],
            ['verpflegungPartner', steuerdaten.verpflegungPartner],
            ['steuerjahr', steuerdaten.steuerjahr?.toString()],
            ['veranlagungsStatus', steuerdaten.veranlagungsStatus],
          ],
          (key) => `shared.form.eltern-steuerdaten.${key}.label`,
        ),
      ]),
    ]),
  ];
};
