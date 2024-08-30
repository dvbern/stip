import { Gesuchstatus, SharedModelGesuch } from '@dv/shared/model/gesuch';

import { isGesuchFormularReadonly } from './shared-util-readonly-state';

const gesuch: SharedModelGesuch = {
  fall: {
    id: '',
    fallNummer: 0,
    mandant: '',
  },
  gesuchsperiode: {
    id: '',
    bezeichnungDe: '',
    bezeichnungFr: '',
    gueltigkeitStatus: 'ENTWURF',
    gesuchsperiodeStart: '',
    gesuchsperiodeStopp: '',
    aufschaltterminStart: '',
    aufschaltterminStopp: '',
    einreichefristNormal: '',
    einreichefristReduziert: '',
    gesuchsjahr: {
      id: '',
      bezeichnungDe: '',
      bezeichnungFr: '',
      technischesJahr: 0,
      gueltigkeitStatus: 'ENTWURF',
    },
    ausbKosten_SekII: 0,
    ausbKosten_Tertiaer: 0,
  },
  gesuchStatus: Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG,
  gesuchNummer: 0,
  id: '',
  aenderungsdatum: '',
  gesuchTrancheToWorkWith: {
    id: '',
    gueltigAb: '',
    gueltigBis: '',
    status: 'UEBERPRUEFEN',
  },
};

describe('when App Gesuchsteller', () => {
  it('should be readonly if in bearbeitung Sachbearbeiter', () => {
    gesuch.gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_SB;

    expect(isGesuchFormularReadonly(gesuch, 'gesuch-app')).toBeTruthy();
  });
});

describe('when App Sachbearbeitung', () => {
  it('should be readonly if in bearbeitung Gesuchsteller', () => {
    gesuch.gesuchStatus = Gesuchstatus.IN_BEARBEITUNG_GS;

    expect(
      isGesuchFormularReadonly(gesuch, 'sachbearbeitung-app'),
    ).toBeTruthy();
  });
});
