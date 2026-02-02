import { TranslocoService, isDefined } from '@jsverse/transloco';
import type { Content } from 'pdfmake/interfaces';

import { GesuchTranche } from '@dv/shared/model/gesuch';
import { niederlassungsStatusConverter } from '@dv/shared/util/gesuch';

import { ExportLand } from '../types';
import { getSection, getTable, getTitle, getValueList } from './generic';
import { _t, formatDate, getBoolean, getLandName } from '../utils/helpers';

export const getPersonInAusbildung = (
  t: TranslocoService,
  tranche: GesuchTranche,
  laender: ExportLand[],
): Content => {
  const personInAusbildung = tranche.gesuchFormular?.personInAusbildung;
  if (!personInAusbildung) {
    return [];
  }
  const adresse = personInAusbildung.adresse;

  const { fluechtlingsstatus, niederlassungsstatus, zustaendigerKanton } =
    niederlassungsStatusConverter.from(personInAusbildung.niederlassungsstatus);

  return [
    getTitle(t, 'shared.person.title', { pageBreak: 'before' }),
    getTable([
      [
        {
          text: _t(t, 'shared.export.shared.sozialversicherungsnummer'),
          bold: true,
        },
        personInAusbildung.sozialversicherungsnummer,
      ],
      ...getValueList(
        t,
        [
          [
            'person.salutation.label',
            _t(t, `shared.form.select.salutation.${personInAusbildung.anrede}`),
          ],
          ['shared.lastname.label', personInAusbildung.nachname],
          ['shared.firstname.label', personInAusbildung.vorname],
          ['shared.address.street.label', adresse.strasse],
          ['shared.address.number.label', adresse.hausnummer],
          ['shared.address.zipcode.label', adresse.plz],
          ['shared.address.city.label', adresse.ort],
          ['shared.address.co.label', adresse.coAdresse],
          [
            'shared.address.country.label',
            getLandName(t, laender, adresse.landId),
          ],
          [
            'shared.alternative-civil-residence.title',
            getBoolean(
              t,
              personInAusbildung.identischerZivilrechtlicherWohnsitz,
            ),
          ],
          [
            'shared.address.zipcode.label',
            personInAusbildung.identischerZivilrechtlicherWohnsitzPLZ,
          ],
          [
            'shared.address.city.label',
            personInAusbildung.identischerZivilrechtlicherWohnsitzOrt,
          ],
        ],
        (key) => `shared.form.${key}`,
      ),
    ]),
    getSection(t, 'shared.form.person.section.additional-information'),
    getTable(
      getValueList(
        t,
        [
          ['person.email.label', personInAusbildung.email],
          ['shared.phone.label', personInAusbildung.telefonnummer],
          [
            'shared.birthday.label',
            formatDate(personInAusbildung.geburtsdatum),
          ],
          [
            'person.zivilstand.label',
            personInAusbildung.zivilstand
              ? _t(
                  t,
                  `shared.form.person.zivilstand.${personInAusbildung.zivilstand}`,
                )
              : '',
          ],
          [
            'person.nationality.label',
            personInAusbildung.nationalitaetId
              ? getLandName(t, laender, personInAusbildung.nationalitaetId)
              : '',
          ],
          ['shared.address.zipcode.label', adresse.plz],
          ['person.origin.label', personInAusbildung.heimatort],
          [
            'person.niederlassungsstatus.label',
            niederlassungsstatus
              ? _t(
                  t,
                  `shared.form.person.niederlassungsstatus.${niederlassungsstatus}`,
                )
              : '',
          ],
          [
            'person.fluechtlingsstatus.label',
            getBoolean(t, fluechtlingsstatus),
          ],
          ['person.zustaendigerKanton.label', zustaendigerKanton],
          ['person.einreisedatum.label', personInAusbildung.einreisedatum],
          [
            'person.zustaendigeKESB.label',
            personInAusbildung.zustaendigeKESB
              ? _t(
                  t,
                  `shared.form.person.zustaendigeKESB.${personInAusbildung.zustaendigeKESB}`,
                )
              : '',
          ],
          [
            'person.sozialhilfebeitraege.label',
            getBoolean(t, personInAusbildung.sozialhilfebeitraege),
          ],
          [
            'shared.wohnsitz.label',
            _t(t, `shared.form.shared.wohnsitz.${personInAusbildung.wohnsitz}`),
          ],
          [
            'shared.wohnsitzanteil.mutter',
            isDefined(personInAusbildung.wohnsitzAnteilMutter)
              ? `${personInAusbildung.wohnsitzAnteilMutter}%`
              : '',
          ],
          [
            'shared.wohnsitzanteil.vater',
            isDefined(personInAusbildung.wohnsitzAnteilVater)
              ? `${personInAusbildung.wohnsitzAnteilVater}%`
              : '',
          ],
          [
            'person.korrespondenzSprache.label',
            _t(
              t,
              `shared.form.person.korrespondenzSprache.${personInAusbildung.korrespondenzSprache}`,
            ),
          ],
          ['person.digital-communication.label', getBoolean(t, true)],
        ],
        (key) => `shared.form.${key}`,
      ),
    ),
  ];
};
