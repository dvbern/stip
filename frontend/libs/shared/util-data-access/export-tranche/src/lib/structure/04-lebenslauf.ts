import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { AbschlussSlim, GesuchTranche } from '@dv/shared/model/gesuch';
import { prepareLanguage } from '@dv/shared/model/type-util';

import {
  getSection,
  getSeparator,
  getTable,
  getTitle,
  getValueList,
} from './generic';
import { _t } from '../utils/helpers';

export const getLebenslaufItems = (
  t: TranslocoService,
  tranche: GesuchTranche,
  abschluesse: AbschlussSlim[],
): Content => {
  const lebenslaufItems = tranche.gesuchFormular?.lebenslaufItems;
  if (!lebenslaufItems?.length) {
    return [];
  }
  const ausbildungen = lebenslaufItems
    .filter((item) => item.abschlussId)
    .map((item) => ({
      ...item,
      type: 'ausbildung' as const,
    }));
  const taetigkeiten = lebenslaufItems
    .filter((item) => !item.abschlussId)
    .map((item) => ({
      ...item,
      type: 'taetigkeit' as const,
    }));
  const getAbschluss = (abschlussId?: string) => {
    return abschluesse.find((a) => a.id === abschlussId);
  };

  return [
    getTitle(t, 'shared.lebenslauf.title', { pageBreak: 'before' }),
    ...(ausbildungen.length > 0
      ? [
          getSection(t, 'shared.export.lebenslauf.ausbildungen'),
          ausbildungen.map((item, i) => {
            const abschluss = getAbschluss(item.abschlussId);
            return [
              ...(i > 0 ? [getSeparator()] : []),
              getTable(
                getValueList(
                  t,
                  [
                    [
                      'subtype.abschluss',
                      abschluss?.[
                        `bezeichnung${prepareLanguage(t.getActiveLang())}`
                      ],
                    ],
                    abschluss?.zusatzfrage
                      ? [
                          `name.zusatzfrage.${abschluss.zusatzfrage}`,
                          item.fachrichtungBerufsbezeichnung,
                        ]
                      : null,
                    [`start.${item.type}`, item.von],
                    [`end.${item.type}`, item.bis],
                    ['wohnsitz', _t(t, `shared.kanton.${item.wohnsitz}`)],
                  ],
                  (key) => `shared.form.lebenslauf.item.${key}`,
                ),
              ),
            ] satisfies Content[];
          }),
        ]
      : []),
    ...(taetigkeiten.length > 0
      ? [
          getSection(t, 'shared.export.lebenslauf.taetigkeiten'),
          taetigkeiten.map(
            (item, i) =>
              [
                ...(i > 0 ? [getSeparator()] : []),
                getTable(
                  getValueList(
                    t,
                    [
                      [
                        'subtype.taetigkeitsart',
                        item.taetigkeitsart
                          ? _t(
                              t,
                              `shared.form.lebenslauf.item.subtype.taetigkeitsart.${item.taetigkeitsart}`,
                            )
                          : '',
                      ],
                      ['name.taetigkeit', item.taetigkeitsBeschreibung],
                      [`start.${item.type}`, item.von],
                      [`end.${item.type}`, item.bis],
                      ['wohnsitz', _t(t, `shared.kanton.${item.wohnsitz}`)],
                    ],
                    (key) => `shared.form.lebenslauf.item.${key}`,
                  ),
                ),
              ] satisfies Content[],
          ),
        ]
      : []),
  ];
};
