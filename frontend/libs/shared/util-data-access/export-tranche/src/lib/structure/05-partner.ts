import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { GesuchTranche } from '@dv/shared/model/gesuch';

import { ExportLand } from '../types';
import { getSection, getTable, getTitle, getValueList } from './generic';
import { _t, formatDate, getBoolean, getLandName } from '../utils/helpers';

export const getPartner = (
  t: TranslocoService,
  tranche: GesuchTranche,
  laender: ExportLand[],
): Content => {
  const partner = tranche.gesuchFormular?.partner;
  if (!partner) {
    return [];
  }

  return [
    getTitle(t, 'shared.partner.title', { pageBreak: 'before' }),
    getSection(t, 'shared.form.shared.section.personal-details'),
    getTable([
      [
        {
          text: _t(t, 'shared.export.shared.sozialversicherungsnummer'),
          bold: true,
        },
        partner.sozialversicherungsnummer,
      ],
      ...getValueList(
        t,
        [
          ['shared.lastname.label', partner.nachname],
          ['shared.firstname.label', partner.vorname],
          ['shared.address.street.label', partner.adresse.strasse],
          ['shared.address.number.label', partner.adresse.hausnummer],
          ['shared.address.zipcode.label', partner.adresse.plz],
          ['shared.address.city.label', partner.adresse.ort],
          ['shared.address.co.label', partner.adresse.coAdresse],
          [
            'shared.address.country.label',
            getLandName(t, laender, partner.adresse.landId),
          ],
          ['shared.birthday.label', formatDate(partner.geburtsdatum)],
          ['partner.inAusbildung.label', getBoolean(t, partner.inAusbildung)],
          [
            'ausbildung.pensum.label',
            partner.ausbildungspensum
              ? _t(
                  t,
                  `shared.form.ausbildung.pensum.${partner.ausbildungspensum}`,
                )
              : '',
          ],
        ],
        (key) => `shared.form.${key}`,
      ),
    ]),
  ];
};
