import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { translatableShared } from '@dv/shared/assets/i18n';
import {
  AppConfig,
  SharedModelCompileTimeConfig,
  ensureIsBusinessAppConfig,
} from '@dv/shared/model/config';
import { GesuchTranche, SharedModelGesuch } from '@dv/shared/model/gesuch';

import { getTable, getTitle, getValueList } from './generic';
import { ExportView, UEBERSICHT_NAME_LIMIT } from '../types';
import { _t, formatDate, getFullname } from '../utils/helpers';

export const getUebersicht = (
  t: TranslocoService,
  defaultComment: string,
  config: SharedModelCompileTimeConfig,
  view: ExportView,
): Content => {
  const { tranche, gesuch, periode, isEditingAenderung, sachbearbeiter } = view;
  return [
    getTitle(t, 'shared.tranche.title'),
    getTable([
      getStatusTuple(t, gesuch, tranche, config.app, isEditingAenderung),
      ...getValueList(
        t,
        [
          [
            'pia.label',
            getFullname(
              tranche.gesuchFormular?.personInAusbildung,
              UEBERSICHT_NAME_LIMIT,
            ),
          ],
          ['gesuchsnummer.label', gesuch.gesuchNummer],
          ['fallnummer.label', gesuch.fallNummer],
          [
            'einreichedatum.label',
            gesuch.einreichedatum ? formatDate(gesuch.einreichedatum) : '',
          ],
          [
            'gesuchsperiode.label',
            periode ? _t(t, 'shared.form.tranche.gesuchsperiode', periode) : '',
          ],
          ['von.label', formatDate(tranche.gueltigAb)],
          ['bis.label', formatDate(tranche.gueltigBis)],
          [
            'einreichefrist.label',
            periode ? formatDate(periode.einreichefrist) : '',
          ],
          ['bemerkung.label', tranche.comment ?? defaultComment],
          ['sachbearbeiter.label', sachbearbeiter],
        ],
        (key) => `shared.form.tranche.${key}`,
      ),
    ]),
  ];
};

const getStatusTuple = (
  t: TranslocoService,
  gesuch: SharedModelGesuch,
  tranche: GesuchTranche,
  appConfig: AppConfig,
  isEditingAenderung: boolean,
): [Content, string] => {
  ensureIsBusinessAppConfig(appConfig);
  return [
    {
      text: _t(
        t,
        translatableShared(
          isEditingAenderung
            ? `shared.form.tranche.status.label.${tranche.typ}`
            : 'shared.form.tranche.status.label.GESUCH',
        ),
      ),
      bold: true,
    },
    _t(
      t,
      translatableShared(
        isEditingAenderung
          ? `shared.gesuch.status.tranche.${tranche.status}`
          : `${appConfig.keyPrefix}.gesuch.status.contract.${gesuch.gesuchStatus}`,
      ),
    ),
  ];
};
