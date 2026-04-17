import { TranslocoService } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { EinnahmenKosten } from '@dv/shared/model/gesuch';

import { getSection, getTable, getTitle, getValueList } from './generic';
import { getBoolean } from '../utils/helpers';

export const getEinnahmenKosten = (
  t: TranslocoService,
  einnahmenKosten: EinnahmenKosten | undefined,
  einkommenTyp: 'partner' | 'pia',
): Content => {
  if (!einnahmenKosten) {
    return [];
  }
  const isPartner = einkommenTyp === 'partner';

  return [
    getTitle(t, `shared.einnahmenkosten${isPartner ? '-partner' : ''}.title`, {
      pageBreak: 'before',
    }),
    getSection(t, 'shared.form.einnahmenkosten.einnahmen.title'),
    getTable([
      ...getValueList(
        t,
        [
          ['nettoerwerbseinkommen', einnahmenKosten.nettoerwerbseinkommen],
          ['arbeitspensumProzent', einnahmenKosten.arbeitspensumProzent],
          ['unterhaltsbeitraege', einnahmenKosten.unterhaltsbeitraege],
          ['zulagen', einnahmenKosten.zulagen],
          ['renten', einnahmenKosten.renten],
          ['eoLeistungen', einnahmenKosten.eoLeistungen],
          ['ergaenzungsleistungen', einnahmenKosten.ergaenzungsleistungen],
          ['beitraege', einnahmenKosten.beitraege],
          ['einnahmenBGSA', einnahmenKosten.einnahmenBGSA],
          ['taggelderAHVIV', einnahmenKosten.taggelderAHVIV],
          ['andereEinnahmen', einnahmenKosten.andereEinnahmen],
        ],
        (key) => `shared.form.einnahmenkosten.${key}.label`,
      ),
    ]),
    getSection(t, 'shared.form.einnahmenkosten.kosten.title'),
    getTable([
      ...getValueList(
        t,
        [
          !isPartner
            ? [
                'ausbildungskosten.label.undefined',
                einnahmenKosten.ausbildungskosten,
              ]
            : null,
          [
            'betreuungskostenKinder.label',
            einnahmenKosten.betreuungskostenKinder,
          ],
          ['fahrkosten.label', einnahmenKosten.fahrkosten],
          !isPartner ? ['wohnkosten.label', einnahmenKosten.wohnkosten] : null,
          isPartner
            ? ['verpflegungskosten.label', einnahmenKosten.verpflegungskosten]
            : null,
          !isPartner
            ? [
                'auswaertigeMittagessenProWoche.label',
                einnahmenKosten.auswaertigeMittagessenProWoche,
              ]
            : null,
          !isPartner
            ? ['wgWohnend.label', getBoolean(t, einnahmenKosten.wgWohnend)]
            : null,
          !isPartner
            ? ['wgAnzahlPersonen.label', einnahmenKosten.wgAnzahlPersonen]
            : null,
          !isPartner
            ? [
                'alternativeWohnformWohnend.label',
                getBoolean(t, einnahmenKosten.alternativeWohnformWohnend),
              ]
            : null,
        ],
        (key) => `shared.form.einnahmenkosten.${key}`,
      ),
    ]),
    getSection(t, 'shared.form.einnahmenkosten.steuerrelevanteDaten.title'),
    getTable([
      ...getValueList(
        t,
        [
          ['vermoegen', einnahmenKosten.vermoegen],
          ['steuerjahr', einnahmenKosten.steuerjahr],
          ['veranlagungsStatus', einnahmenKosten.veranlagungsStatus],
          ['steuern', einnahmenKosten.steuern],
        ],
        (key) => `shared.form.einnahmenkosten.${key}.label`,
      ),
    ]),
  ];
};
