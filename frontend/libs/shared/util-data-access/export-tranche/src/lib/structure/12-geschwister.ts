import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { GesuchTranche } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';

import { getSeparator, getTable, getTitle, getValueList } from './generic';
import { _t, formatDate } from '../utils/helpers';

export const getGeschwisters = (
  t: TranslocoService,
  tranche: GesuchTranche,
): Content => {
  const geschwisters = tranche.gesuchFormular?.geschwisters;
  if (!geschwisters?.length) {
    return [];
  }

  return [
    getTitle(t, 'shared.geschwister.title', { pageBreak: 'before' }),
    geschwisters.map((geschwister, i) => [
      ...(i > 0 ? [getSeparator()] : []),
      getTable([
        ...getValueList(
          t,
          [
            ['shared.lastname.label', geschwister.nachname],
            ['shared.firstname.label', geschwister.vorname],
            ['shared.birthday.label', formatDate(geschwister.geburtsdatum)],
            [
              'shared.wohnsitz.label',
              _t(t, `shared.form.shared.wohnsitz.${geschwister.wohnsitz}`),
            ],
            [
              'shared.wohnsitzanteil.mutter',
              isDefined(geschwister.wohnsitzAnteilMutter)
                ? `${geschwister.wohnsitzAnteilMutter}%`
                : '',
            ],
            [
              'shared.wohnsitzanteil.vater',
              isDefined(geschwister.wohnsitzAnteilVater)
                ? `${geschwister.wohnsitzAnteilVater}%`
                : '',
            ],
            [
              'geschwister.ausbildungssituation.label',
              _t(
                t,
                `shared.form.geschwister.ausbildungssituation.select.${geschwister.ausbildungssituation}`,
              ),
            ],
          ],
          (key) => `shared.form.${key}`,
        ),
      ]),
    ]),
  ];
};
