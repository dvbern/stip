import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { ElternTyp, GesuchTranche } from '@dv/shared/model/gesuch';

import { ExportLand } from '../types';
import { getSection, getTable, getTitle, getValueList } from './generic';
import { _t, formatDate, getBoolean, getLandName } from '../utils/helpers';

export const getEltern = (
  t: TranslocoService,
  tranche: GesuchTranche,
  laender: ExportLand[],
): Content => {
  const eltern = tranche.gesuchFormular?.elterns;
  if (!eltern?.length) {
    return [];
  }

  return [
    getTitle(t, 'shared.eltern.title', { pageBreak: 'before' }),
    eltern.map((elternteil, i) => [
      getSection(
        t,
        `shared.form.eltern.${elternteil.elternTyp}.title`,
        i > 0 ? { pageBreak: 'before' } : undefined,
      ),
      getTable([
        [
          {
            text: _t(t, 'shared.export.shared.sozialversicherungsnummer'),
            bold: true,
          },
          elternteil.sozialversicherungsnummer ?? '',
        ],
        ...getValueList(
          t,
          [
            ['shared.lastname.label', elternteil.nachname],
            ['shared.firstname.label', elternteil.vorname],
            ['shared.address.street.label', elternteil.adresse.strasse],
            ['shared.address.number.label', elternteil.adresse.hausnummer],
            ['shared.address.zipcode.label', elternteil.adresse.plz],
            ['shared.address.city.label', elternteil.adresse.ort],
            ['shared.address.co.label', elternteil.adresse.coAdresse],
            [
              'shared.address.country.label',
              getLandName(t, laender, elternteil.adresse.landId),
            ],
            [
              'shared.alternative-civil-residence.title',
              getBoolean(t, elternteil.identischerZivilrechtlicherWohnsitz),
            ],
            [
              'shared.address.zipcode.label',
              elternteil.identischerZivilrechtlicherWohnsitzPLZ,
            ],
            [
              'shared.address.city.label',
              elternteil.identischerZivilrechtlicherWohnsitzOrt,
            ],
            [
              'shared.birthday.label',
              elternteil.geburtsdatum
                ? formatDate(elternteil.geburtsdatum)
                : '',
            ],
            ['shared.wohnkosten.label', elternteil.wohnkosten],
            ['shared.phone.label', elternteil.telefonnummer],
            [
              'eltern.sozialhilfebeitraege.label',
              getBoolean(t, elternteil.sozialhilfebeitraege),
            ],
          ],
          (key) => `shared.form.${key}`,
        ),
        [
          {
            text: _t(
              t,
              `shared.form.eltern.ausweisbFluechtling.${elternteil.elternTyp === ElternTyp.MUTTER ? 'mutter' : 'vater'}.label`,
            ),
            bold: true,
          },
          getBoolean(t, elternteil.ausweisbFluechtling),
        ],
      ]),
    ]),
  ];
};
