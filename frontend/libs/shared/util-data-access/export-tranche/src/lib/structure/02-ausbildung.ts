import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { GesuchTranche } from '@dv/shared/model/gesuch';
import { prepareLanguage, type } from '@dv/shared/model/type-util';

import { Styles } from '../types';
import { getSection, getTable, getTitle, getValueList } from './generic';
import { _t, getBoolean } from '../utils/helpers';

export const getAusbildung = (
  t: TranslocoService,
  tranche: GesuchTranche,
): Content => {
  const ausbildung = tranche.gesuchFormular?.ausbildung;
  const ausbildungsstaette =
    ausbildung?.alternativeAusbildungsgang ??
    ausbildung?.ausbildungsgang?.ausbildungsstaette?.[
      `name${prepareLanguage(t.getActiveLang())}`
    ];
  const ausbildungsgang =
    ausbildung?.alternativeAusbildungsgang ??
    ausbildung?.ausbildungsgang?.abschluss?.[
      `bezeichnung${prepareLanguage(t.getActiveLang())}`
    ];
  const zusatzfrage = ausbildung?.ausbildungsgang?.abschluss.zusatzfrage;
  if (!ausbildung) {
    return [];
  }
  return [
    getTitle(t, 'shared.ausbildung.title', { pageBreak: 'before' }),
    getSection(t, 'shared.form.ausbildung.place.title'),
    getTable(
      getValueList(
        t,
        [
          ['ausbildung.ausbildungsstaette.label', ausbildungsstaette],
          ['ausbildung.ausbildungsgang.label', ausbildungsgang],
          zusatzfrage
            ? [
                `ausbildung.fachrichtung.label.${zusatzfrage}`,
                ausbildung.fachrichtungBerufsbezeichnung,
              ]
            : null,
          ['ausbildung.ausbildungsort.label', ausbildung.ausbildungsort],
          ['shared.address.zipcode.label', ausbildung.ausbildungsortPLZ],
          [
            'ausbildung.isAusbildungAusland.label',
            getBoolean(t, ausbildung.isAusbildungAusland),
          ],
        ],
        (key) => `shared.form.${key}`,
      ),
    ),
    {
      text: _t(t, 'shared.form.ausbildung.duration.title'),
      style: type<Styles>('section'),
    },
    getTable(
      getValueList(
        t,
        [
          ['start', ausbildung.ausbildungBegin],
          ['ende', ausbildung.ausbildungEnd],
          [
            'pensum',
            ausbildung.pensum
              ? _t(t, `shared.form.ausbildung.pensum.${ausbildung.pensum}`)
              : '',
          ],
        ],
        (key) => `shared.form.ausbildung.${key}.label`,
      ),
    ),
  ];
};
