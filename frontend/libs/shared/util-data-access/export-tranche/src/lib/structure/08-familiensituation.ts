import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { Familiensituation, GesuchTranche } from '@dv/shared/model/gesuch';
import { isDefined, lowercased } from '@dv/shared/model/type-util';

import { getTable, getTitle, getValueList } from './generic';
import { _t, getBoolean } from '../utils/helpers';

export const getFamiliensituation = (
  t: TranslocoService,
  tranche: GesuchTranche,
): Content => {
  const familiensituation = tranche.gesuchFormular?.familiensituation;
  if (!familiensituation) {
    return [];
  }

  return [
    getTitle(t, 'shared.familiensituation.title', { pageBreak: 'before' }),
    getTable(
      getValueList(
        t,
        [
          [
            'leibliche-eltern-verheiratet-konkubinat.label',
            getBoolean(t, familiensituation.elternVerheiratetZusammen),
          ],
          !familiensituation.elternVerheiratetZusammen
            ? [
                'gerichtliche-alimentenregelung.label',
                getBoolean(t, familiensituation.gerichtlicheAlimentenregelung),
              ]
            : null,
          !familiensituation.elternVerheiratetZusammen &&
          familiensituation.gerichtlicheAlimentenregelung
            ? [
                'wer-zahlt-alimente.label',
                familiensituation.werZahltAlimente
                  ? _t(
                      t,
                      `shared.form.familiensituation.${lowercased(familiensituation.werZahltAlimente)}`,
                    )
                  : '',
              ]
            : null,
          !familiensituation.elternVerheiratetZusammen &&
          !familiensituation.gerichtlicheAlimentenregelung
            ? [
                'elternteil-verstorben-unbekannt.label',
                getBoolean(t, familiensituation.elternteilUnbekanntVerstorben),
              ]
            : null,
          familiensituation.elternteilUnbekanntVerstorben
            ? ['elternteil-verstorben.label', '', { colSpan: 2 }]
            : null,
          ...(familiensituation.elternteilUnbekanntVerstorben
            ? (['mutter', 'vater'] as const).flatMap(
                (elternteil) =>
                  [
                    [
                      elternteil,
                      getUnbekanntVerstorbenOrKeine(
                        t,
                        elternteil,
                        familiensituation,
                      ),
                    ],
                    ...(familiensituation[`${elternteil}UnbekanntGrund`]
                      ? ([
                          [
                            `${elternteil}-unbekannt-reason.label`,
                            getUnbekanntReason(
                              t,
                              elternteil,
                              familiensituation,
                            ),
                          ],
                        ] as const)
                      : []),
                    ...(isDefined(
                      familiensituation[`${elternteil}Wiederverheiratet`],
                    )
                      ? ([
                          [
                            `${elternteil}-wiederverheiratet.label`,
                            getBoolean(
                              t,
                              familiensituation[
                                `${elternteil}Wiederverheiratet`
                              ],
                            ),
                          ],
                        ] as const)
                      : []),
                  ] as const,
              )
            : []),
          ...(!familiensituation.elternteilUnbekanntVerstorben
            ? ([
                isDefined(familiensituation.mutterWiederverheiratet)
                  ? ([
                      'mutter-wiederverheiratet.label',
                      getBoolean(t, familiensituation.mutterWiederverheiratet),
                    ] as const)
                  : null,
                isDefined(familiensituation.vaterWiederverheiratet)
                  ? ([
                      'vater-wiederverheiratet.label',
                      getBoolean(t, familiensituation.vaterWiederverheiratet),
                    ] as const)
                  : null,
              ] as const)
            : []),
        ],
        (key) => `shared.form.familiensituation.${key}`,
      ),
    ),
  ];
};

const getUnbekanntVerstorbenOrKeine = (
  t: TranslocoService,
  elternteil: 'mutter' | 'vater',
  familiensituation: Familiensituation,
): string => {
  switch (familiensituation[`${elternteil}UnbekanntVerstorben`]) {
    case 'UNBEKANNT':
      return _t(
        t,
        'shared.form.familiensituation.elternteil-verstorben-unbekannt.unbekannt',
      );
    case 'VERSTORBEN':
      return _t(
        t,
        'shared.form.familiensituation.elternteil-verstorben-unbekannt.verstorben',
      );
    case 'WEDER_NOCH':
      return _t(
        t,
        'shared.form.familiensituation.elternteil-verstorben-unbekannt.keine',
      );
  }
  return '';
};

const getUnbekanntReason = (
  t: TranslocoService,
  elternteil: 'mutter' | 'vater',
  familiensituation: Familiensituation,
): string => {
  switch (familiensituation[`${elternteil}UnbekanntGrund`]) {
    case 'UNBEKANNTER_AUFENTHALTSORT':
      return _t(
        t,
        'shared.form.familiensituation.elternteil-unbekannt-reason.unbekannter-aufenthaltsort',
      );
    case 'FEHLENDE_ANERKENNUNG':
      return _t(
        t,
        `shared.form.familiensituation.elternteil-unbekannt-reason.fehlende-${elternteil}schaftsanerkennung`,
      );
  }
  return '';
};
