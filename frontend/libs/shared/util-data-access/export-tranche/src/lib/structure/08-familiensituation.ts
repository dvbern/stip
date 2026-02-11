import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { Familiensituation, GesuchTranche } from '@dv/shared/model/gesuch';
import { lowercased } from '@dv/shared/model/type-util';

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
          [
            'gerichtliche-alimentenregelung.label',
            getBoolean(t, familiensituation.gerichtlicheAlimentenregelung),
          ],
          [
            'wer-zahlt-alimente.label',
            familiensituation.werZahltAlimente
              ? _t(
                  t,
                  `shared.form.familiensituation.${lowercased(familiensituation.werZahltAlimente)}`,
                )
              : '',
          ],
          [
            'elternteil-verstorben-unbekannt.label',
            getBoolean(t, familiensituation.elternteilUnbekanntVerstorben),
          ],
          ...(['mutter', 'vater'] as const).flatMap((elternteil) =>
            familiensituation.elternteilUnbekanntVerstorben
              ? ([
                  ['elternteil-verstorben.label', ''],
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
                          getUnbekanntReason(t, elternteil, familiensituation),
                        ],
                      ] as const)
                    : []),
                ] as const)
              : [],
          ),
          [
            'mutter-wiederverheiratet.label',
            getBoolean(t, familiensituation.mutterWiederverheiratet),
          ],
          [
            'vater-wiederverheiratet.label',
            getBoolean(t, familiensituation.vaterWiederverheiratet),
          ],
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
  if (familiensituation[`${elternteil}UnbekanntVerstorben`]) {
    return _t(
      t,
      'shared.form.familiensituation.elternteil-verstorben-unbekannt.verstorben',
    );
  }
  if (familiensituation[`${elternteil}UnbekanntGrund`]) {
    return _t(
      t,
      'shared.form.familiensituation.elternteil-verstorben-unbekannt.unbekannt',
    );
  }
  return _t(
    t,
    'shared.form.familiensituation.elternteil-verstorben-unbekannt.keine',
  );
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
