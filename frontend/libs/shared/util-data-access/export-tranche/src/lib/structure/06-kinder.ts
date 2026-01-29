import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { GesuchTranche } from '@dv/shared/model/gesuch';

import { getSeparator, getTable, getTitle, getValueList } from './generic';
import { _t, formatDate } from '../utils/helpers';

export const getKinder = (
  t: TranslocoService,
  tranche: GesuchTranche,
): Content => {
  const kinder = tranche.gesuchFormular?.kinds;
  if (!kinder?.length) {
    return [];
  }

  return [
    getTitle(t, 'shared.kinder.title', { pageBreak: 'before' }),
    kinder.map((kind, i) => [
      ...(i > 0 ? [getSeparator()] : []),
      getTable([
        ...getValueList(
          t,
          [
            ['shared.lastname', kind.nachname],
            ['shared.firstname', kind.vorname],
            [
              'shared.birthday',
              kind.geburtsdatum ? formatDate(kind.geburtsdatum) : '',
            ],
            ['shared.wohnsitzAnteilPia', `${kind.wohnsitzAnteilPia}%`],
            [
              'geschwister.ausbildungssituation',
              _t(
                t,
                `shared.form.geschwister.ausbildungssituation.select.${kind.ausbildungssituation}`,
              ),
            ],
            ['kinder.unterhaltsbeitraege', kind.unterhaltsbeitraege],
            [
              'kinder.kinderUndAusbildungszulagen',
              kind.kinderUndAusbildungszulagen,
            ],
            ['kinder.renten', kind.renten],
            ['kinder.ergaenzungsleistungen', kind.ergaenzungsleistungen],
            ['kinder.andereEinnahmen', kind.andereEinnahmen],
          ],
          (key) => `shared.form.${key}.label`,
        ),
      ]),
    ]),
  ];
};
